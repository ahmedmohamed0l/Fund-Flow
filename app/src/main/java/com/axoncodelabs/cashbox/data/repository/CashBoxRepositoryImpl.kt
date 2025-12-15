package com.axoncodelabs.cashbox.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.Transaction
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.ExpenseWithFund
import com.axoncodelabs.cashbox.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CashBoxRepositoryImpl @Inject constructor(
    private val fundDao: FundDao,
    private val transactionDao: TransactionDao,
    private val dataStore: DataStore<Preferences>,
) : CashBoxRepository {
    //-----------------[ Throw Exception ]-----------------
    private suspend fun getFundOrThrow(id: Int): FundEntity {
        return fundDao.getFundById(id)
            ?: throw IllegalArgumentException("Fund with id $id not found")
    }

    private suspend fun getTransactionOrThrow(id: Int): TransactionEntity {
        return transactionDao.getTransactionById(id)
            ?: throw IllegalArgumentException("Transaction with id $id not found")
    }

    //-----------------[ Funds ]-----------------
    override suspend fun insertFund(fund: FundEntity): Long {
        return fundDao.insertFund(fund)
    }

    override suspend fun updateFund(fund: FundEntity) {
        fundDao.updateFund(fund)
    }

    override suspend fun deleteFund(fund: FundEntity) {
        fundDao.deleteFund(fund)
    }

    override suspend fun deleteAllFundTransactions(fundId: Int) {
        val fund = getFundOrThrow(fundId)

        val transactions = transactionDao.getTransactionsByFundId(fundId).first()

        var correctedBalance = fund.balance
        transactions.forEach { t -> correctedBalance -= (signForType(t.type) * t.amount) }

        fundDao.updateFund(fund.copy(balance = correctedBalance))

        transactionDao.deleteTransactionsByFundId(fundId)
    }

    override fun getAllFunds(): Flow<List<FundEntity>> {
        return fundDao.getAllFunds()
    }

    override suspend fun getFundById(id: Int): FundEntity? {
        return fundDao.getFundById(id)
    }

    override fun getTotalBalance(): Flow<Double> {
        return fundDao.getTotalBalance()
    }

    // helper: sign of transaction for balance calculation
    private fun signForType(type: TransactionType): Int =
        when (type) {
            TransactionType.INCOME -> 1
            TransactionType.EXPENSE -> -1
        }

    //-----------------[ Transactions ]-----------------
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

    override suspend fun getTransactionById(id: Int): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }

    override fun getExpensesByDate(
        startDate: Long, endDate: Long,
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getExpensesByDate(startDate, endDate)
    }

    override fun getExpensesWithFundByDate(
        startDate: Long,
        endDate: Long
    ): Flow<List<ExpenseWithFund>> {
        return transactionDao.getExpensesWithFundByDate(startDate, endDate)
    }

    override fun getExpensesSumByDate(
        startDate: Long, endDate: Long,
    ): Flow<Double> {
        return transactionDao.getExpensesSumByDate(startDate, endDate)
    }

    override fun getTransactionsByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByType(fundId, type, startDate, endDate)
    }

    override fun getTransactionsSumByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double> {
        return transactionDao.getTransactionsSumByType(fundId, type, startDate, endDate)
    }

    override fun getFundIncomeSumFlow(
        fundId: Int,
        startDate: Long,
        endDate: Long,
    ): Flow<Double> {
        return transactionDao.getTransactionsSumByType(
            fundId,
            TransactionType.INCOME,
            startDate,
            endDate
        )
    }

    override fun getFundExpenseSumFlow(
        fundId: Int,
        startDate: Long,
        endDate: Long,
    ): Flow<Double> {
        return transactionDao.getTransactionsSumByType(
            fundId,
            TransactionType.EXPENSE,
            startDate,
            endDate
        )
    }

    //-----------------[ Funds Transfer ]-----------------
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

    //-----------------[ Preferences ]-----------------

    //......( Keys )......

    private object Keys {
        val THEME_KEY = stringPreferencesKey("theme")
        val HIDE_KEY = booleanPreferencesKey("is_hide")
    }


    //......( Read Flow Impl )......

    override val themeFlow: Flow<Theme> = dataStore.data.map {
        when (it[Keys.THEME_KEY]) {
            "dark" -> Theme.Dark
            else -> Theme.Light
        }
    }

    override val hideDataFlow: Flow<Boolean> = dataStore.data.map {
        it[Keys.HIDE_KEY] ?: false
    }


    //......( Save Impl )......

    override suspend fun saveTheme(theme: Theme) {
        dataStore.edit { it[Keys.THEME_KEY] = theme.value }
    }

    override suspend fun saveHideData(isHide: Boolean) {
        dataStore.edit { it[Keys.HIDE_KEY] = isHide }
    }
}