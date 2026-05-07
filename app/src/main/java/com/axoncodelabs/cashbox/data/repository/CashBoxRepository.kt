package com.axoncodelabs.cashbox.data.repository

import com.axoncodelabs.cashbox.data.local.dao.TransactionDao.DateRange
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.data.util.backup.BackupInfo
import com.axoncodelabs.cashbox.ui.theme.Theme
import kotlinx.coroutines.flow.Flow

interface CashBoxRepository {
    // ────────────────{ Backup & Restore }────────────────
    suspend fun createBackup()
    suspend fun getAvailableBackups(): List<BackupInfo>
    suspend fun restoreBackup(fileName: String)
    suspend fun deleteBackup(fileName: String)

    // ────────────────{ Fund Actions }────────────────
    suspend fun insertFund(fund: FundEntity): Long
    suspend fun updateFund(fund: FundEntity)
    suspend fun deleteFund(fund: FundEntity)

    //──── For (Funds_Screen) ────
    fun getAllFunds(): Flow<List<FundEntity>>

    suspend fun deleteAllFundTransactions(fundId: Int)

    // ────────────────{ Transaction Actions }────────────────
    suspend fun insertTransaction(transaction: TransactionEntity)
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)

    //──── For (Expenses_Screen) ────
    fun getExpensesByDateAndType(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithFund>>

    //──── For (Reports_Screen) ────
    fun getFirstAndLastDate(): Flow<DateRange>

    fun getAllDates(): Flow<List<Long>>

    fun getTransactionsByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithFund>>

    fun getTransactionsByFundAndDateAndType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithFund>>

    // ────────────────{ Funds Transfer }────────────────
    suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long = System.currentTimeMillis()
    )

    // ────────────────{ Preferences }────────────────
    //──── Read Flow ────
    val themeFlow: Flow<Theme>
    val hideDataFlow: Flow<Boolean>
    val lastBackupDateFlow: Flow<Long?>
    val autoBackupFlow: Flow<Boolean>

    //──── Save ────
    suspend fun saveTheme(theme: Theme)
    suspend fun saveHideData(isHide: Boolean)
    suspend fun saveAutoBackup(isAutoBackup: Boolean)
}