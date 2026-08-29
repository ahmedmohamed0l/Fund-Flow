package com.axoncodelabs.fundflow.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.Transaction
import androidx.room.withTransaction
import com.axoncodelabs.fundflow.data.local.FundFlowDatabase
import com.axoncodelabs.fundflow.data.local.dao.FundDao
import com.axoncodelabs.fundflow.data.local.dao.TransactionDao
import com.axoncodelabs.fundflow.data.local.dao.TransactionDao.DateRange
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionType
import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund
import com.axoncodelabs.fundflow.data.util.backup.BackupData
import com.axoncodelabs.fundflow.data.util.backup.BackupFileManager
import com.axoncodelabs.fundflow.data.util.backup.BackupInfo
import com.axoncodelabs.fundflow.data.util.backup.BackupSerializer
import com.axoncodelabs.fundflow.ui.theme.Theme
import com.axoncodelabs.fundflow.ui.util.toMillisFromBackup
import com.axoncodelabs.fundflow.util.language.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundFlowRepositoryImpl @Inject constructor(
    private val db: FundFlowDatabase,
    private val fundDao: FundDao,
    private val transactionDao: TransactionDao,
    private val dataStore: DataStore<Preferences>,
    private val backupFileManager: BackupFileManager,
    private val backupSerializer: BackupSerializer
) : FundFlowRepository {

    // ────────────────{ Throw Exception }────────────────
    private suspend fun getFundOrThrow(id: Int): FundEntity {
        return fundDao.getFundById(id)
            ?: throw IllegalArgumentException("Fund with id $id not found")
    }

    private suspend fun getTransactionOrThrow(id: Int): TransactionEntity {
        return transactionDao.getTransactionById(id)
            ?: throw IllegalArgumentException("Transaction with id $id not found")
    }

    //──── Helpers ────
    // Sign of transaction for balance calculation
    private fun signForType(type: TransactionType): Int =
        when (type) {
            TransactionType.INCOME -> 1
            TransactionType.EXPENSE -> -1
        }

    // ────────────────{ Backup & Restore }────────────────
    override suspend fun createBackup(isAuto: Boolean) {
        withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()

            if (isAuto) {
                backupFileManager.deleteAutoBackupsForDate(timestamp)
            }

            val data = BackupData(
                funds = fundDao.getAllFundsList(),
                transactions = transactionDao.getAllTransactionsList()
            )

            val json = backupSerializer.serialize(data)
                ?: throw IOException("Serialization failed")

            backupFileManager.saveBackupFile(
                json,
                timestamp,
                isAuto
            ) ?: throw IOException("Could not save backup file")

            val backups = getAvailableBackups()

            if (backups.size >= 10) {
                val oldest = backups.minByOrNull { it.dateTimeMillis }
                oldest?.let {
                    deleteBackup(oldest.folderName)
                }
            }

            syncLastBackupDate()
        }
    }

    override suspend fun getAvailableBackups(): List<BackupInfo> {
        val files = backupFileManager.listBackupFiles()
        return files.mapNotNull { file ->
            val dateStr = file.nameWithoutExtension.removePrefix("backup_")
            val millis = dateStr.toMillisFromBackup() ?: return@mapNotNull null

            val isAuto: Boolean? = when {
                dateStr.endsWith("_a") -> true
                dateStr.endsWith("_m") -> false
                else -> null
            }

            BackupInfo(
                folderName = file.name,
                dateTimeMillis = millis,
                isAuto = isAuto
            )
        }
    }

    override suspend fun restoreBackup(fileName: String) {
        withContext(Dispatchers.IO) {

            val file = backupFileManager.readBackupFile(fileName)
                ?: throw IOException("Could not read backup file")

            val data = backupSerializer.deserialize(file)
                ?: throw IOException("Deserialization failed")

            db.withTransaction {
                transactionDao.deleteAllTransactions()
                fundDao.deleteAllFunds()

                data.funds.forEach { fundDao.insertFund(it) }
                data.transactions.forEach { transactionDao.insertTransaction(it) }
            }
        }
    }

    override suspend fun deleteBackup(fileName: String) {
        backupFileManager.deleteBackupFile(fileName)
        syncLastBackupDate()
    }

    //── Last Date Helper ──
    private suspend fun syncLastBackupDate() {
        val latestMillis = getAvailableBackups()
            .maxByOrNull { it.dateTimeMillis }
            ?.dateTimeMillis

        dataStore.edit { prefs ->
            if (latestMillis == null) {
                prefs.remove(Keys.LAST_BACKUP_KEY)
            } else {
                prefs[Keys.LAST_BACKUP_KEY] = latestMillis
            }
        }
    }

    // ────────────────{ Fund Actions }────────────────
    override suspend fun insertFund(fund: FundEntity): Long {
        return fundDao.insertFund(fund)
    }

    override suspend fun updateFund(fund: FundEntity) {
        fundDao.updateFund(fund)
    }

    @Transaction
    override suspend fun deleteFund(fund: FundEntity) {
        fundDao.deleteFund(fund)
        transactionDao.deleteAllTransactionsByFundId(fund.id)
    }

    //──── For (Funds_Screen) ────
    override fun getAllFunds(): Flow<List<FundEntity>> {
        return fundDao.getAllFunds()
    }

    override suspend fun deleteAllFundTransactions(fundId: Int) {
        val fund = getFundOrThrow(fundId)

        fundDao.updateFund(fund.copy(balance = 0.0))
        transactionDao.deleteAllTransactionsByFundId(fundId)
    }

    // ────────────────{ Transaction Actions }────────────────
    @Transaction
    override suspend fun insertTransaction(transaction: TransactionEntity) {
        val fund = getFundOrThrow(transaction.fundId)

        // delta = +amount for income, -amount for expense
        val delta = signForType(transaction.type) * transaction.amount

        // update cached balance then insert transaction
        fundDao.updateFund(fund.copy(balance = fund.balance + delta))
        transactionDao.insertTransaction(transaction)
    }

    @Transaction
    override suspend fun updateTransaction(transaction: TransactionEntity) {
        // fetch old transaction
        val old = getTransactionOrThrow(transaction.id)

        // if fund didn't change -> single fund adjust by delta
        if (old.fundId == transaction.fundId) {
            val delta = (signForType(transaction.type) * transaction.amount) -
                    (signForType(old.type) * old.amount)
            val fund = getFundOrThrow(transaction.fundId)
            fundDao.updateFund(fund.copy(balance = fund.balance + delta))

        } else {
            // fund changed: reverse old effect from old fund, apply new effect to new fund
            val oldFund = getFundOrThrow(old.fundId)
            val newFund = getFundOrThrow(transaction.fundId)

            val oldEffect = signForType(old.type) * old.amount
            val newEffect = signForType(transaction.type) * transaction.amount

            fundDao.updateFund(oldFund.copy(balance = oldFund.balance - oldEffect))
            fundDao.updateFund(newFund.copy(balance = newFund.balance + newEffect))
        }

        // finally update transaction row
        transactionDao.updateTransaction(transaction)
    }

    @Transaction
    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        // reverse effect of this transaction on its fund then delete
        val fund = getFundOrThrow(transaction.fundId)

        val reverseDelta = -(signForType(transaction.type) * transaction.amount)
        fundDao.updateFund(fund.copy(balance = fund.balance + reverseDelta))

        transactionDao.deleteTransaction(transaction)
    }

    //──── For (Expenses_Screen) ────
    override fun getExpensesByDateAndType(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>> {
        return transactionDao.getExpensesByDateAndType(startDate, endDate)
    }

    //──── For (Reports_Screen) ────
    override fun getFirstAndLastDate(): Flow<DateRange> {
        return transactionDao.getFirstAndLastDate()
    }

    override fun getAllDates(): Flow<List<Long>> {
        return transactionDao.getAllDates()
    }

    override fun getTransactionsByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>> {
        return transactionDao.getTransactionsByDateAndType(type, startDate, endDate)
    }

    override fun getTransactionsByFundAndDateAndType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>> {
        return transactionDao.getTransactionsByFundAndDateAndType(fundId, type, startDate, endDate)
    }

    // ────────────────{ Funds Transfer }────────────────
    @Transaction
    override suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long,
    ) {
        val expenseTransaction = TransactionEntity(
            fundId = fromFundId,
            amount = amount,
            description = description,
            date = timestamp,
            type = TransactionType.EXPENSE,
            isTransfer = true
        )
        val incomeTransaction = TransactionEntity(
            fundId = toFundId,
            amount = amount,
            description = description,
            date = timestamp,
            type = TransactionType.INCOME,
            isTransfer = true
        )
        insertTransaction(expenseTransaction)
        insertTransaction(incomeTransaction)
    }

    // ────────────────{ Preferences }────────────────
    //──── Keys ────
    private object Keys {
        val THEME_KEY = stringPreferencesKey("theme")
        val HIDE_KEY = booleanPreferencesKey("is_hide")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val LAST_BACKUP_KEY = longPreferencesKey("last_backup_date")
        val AUTO_BACKUP_KEY = booleanPreferencesKey("auto_backup_option")
    }

    //──── Read Flow ────
    override val themeFlow: Flow<Theme> = dataStore.data.map {
        when (it[Keys.THEME_KEY]) {
            "dark" -> Theme.Dark
            else -> Theme.Light
        }
    }

    override val hideDataFlow: Flow<Boolean> = dataStore.data.map {
        it[Keys.HIDE_KEY] ?: false
    }

    override val languageFlow: Flow<Language> = dataStore.data.map { preferences ->
        Language.entries.firstOrNull {
            it.languageTag == preferences[Keys.LANGUAGE_KEY]
        } ?: Language.Arabic
    }

    override val lastBackupDateFlow: Flow<Long?> = dataStore.data.map { it[Keys.LAST_BACKUP_KEY] }

    override val autoBackupFlow: Flow<Boolean> = dataStore.data.map {
        it[Keys.AUTO_BACKUP_KEY] ?: false
    }

    //──── Save ────
    override suspend fun saveTheme(theme: Theme) {
        dataStore.edit { it[Keys.THEME_KEY] = theme.value }
    }

    override suspend fun saveHideData(isHide: Boolean) {
        dataStore.edit { it[Keys.HIDE_KEY] = isHide }
    }

    override suspend fun saveLanguage(language: Language) {
        dataStore.edit { it[Keys.LANGUAGE_KEY] = language.languageTag }
    }

    override suspend fun saveAutoBackup(isAutoBackup: Boolean) {
        dataStore.edit { it[Keys.AUTO_BACKUP_KEY] = isAutoBackup }
    }
}