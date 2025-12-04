package com.axoncodelabs.cashbox.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.Transaction
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CashBoxRepositoryImpl @Inject constructor(
    private val fundDao: FundDao,
    private val transactionDao: TransactionDao,
    private val dataStore: DataStore<Preferences>,
) : CashBoxRepository {

    private object Keys {
        val THEME_KEY = stringPreferencesKey("theme")
    }

    // Funds
    override suspend fun insertFund(fund: FundEntity) {
        fundDao.insertFund(fund)
    }

    override suspend fun updateFund(fund: FundEntity) {
        fundDao.updateFund(fund)
    }

    override suspend fun deleteFund(fund: FundEntity) {
        fundDao.deleteFund(fund)
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

    // Transactions
    @Transaction
    override suspend fun insertTransaction(transaction: TransactionEntity) {
        val fund = fundDao.getFundById(transaction.fundId)
            ?: throw IllegalArgumentException("Fund with id ${transaction.fundId} not found")

        // delta = +amount for income, -amount for expense
        val delta = signForType(transaction.type) * transaction.amount

        // update cached balance then insert transaction (transactional because of @Transaction)
        fundDao.updateFund(fund.copy(balance = fund.balance + delta))
        transactionDao.insertTransaction(transaction)
    }

    @Transaction
    override suspend fun updateTransaction(transaction: TransactionEntity) {
        // fetch old transaction
        val old = transactionDao.getTransactionById(transaction.id)
            ?: throw IllegalArgumentException("Transaction with id ${transaction.id} not found")

        // if fund didn't change -> single fund adjust by delta
        if (old.fundId == transaction.fundId) {
            val delta = (signForType(transaction.type) * transaction.amount) -
                    (signForType(old.type) * old.amount)
            val fund = fundDao.getFundById(transaction.fundId)
                ?: throw IllegalArgumentException("Fund with id ${transaction.fundId} not found")
            fundDao.updateFund(fund.copy(balance = fund.balance + delta))
        } else {
            // fund changed: reverse old effect from old fund, apply new effect to new fund
            val oldFund = fundDao.getFundById(old.fundId)
                ?: throw IllegalArgumentException("Fund with id ${old.fundId} not found")
            val newFund = fundDao.getFundById(transaction.fundId)
                ?: throw IllegalArgumentException("Fund with id ${transaction.fundId} not found")

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
        val fund = fundDao.getFundById(transaction.fundId)
            ?: throw IllegalArgumentException("Fund with id ${transaction.fundId} not found")

        val reverseDelta = -(signForType(transaction.type) * transaction.amount)
        fundDao.updateFund(fund.copy(balance = fund.balance + reverseDelta))

        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun getTransactionById(id: Int): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }

    override fun getTransactionsByDate(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByDate(startDate, endDate)
    }

    override fun getTransactionsSumByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double> {
        return transactionDao.getTransactionsSumByType(fundId, type, startDate, endDate)
    }

    override fun getTransactionsByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByType(fundId, type, startDate, endDate)
    }

    override fun getExpensesByDate(
        startDate: Long, endDate: Long,
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getExpensesByDate(startDate, endDate)
    }

    // convenience flows: مجموع الإيرادات و مجموع المصروفات و قيمة الصندوق محسوبة من المعاملتين
    override fun getFundIncomeSumFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double> {
        return transactionDao.getTransactionsSumByType(
            fundId,
            TransactionType.INCOME,
            startDate,
            endDate
        )
    }

    override fun getFundExpenseSumFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double> {
        return transactionDao.getTransactionsSumByType(
            fundId,
            TransactionType.EXPENSE,
            startDate,
            endDate
        )
    }

    // computed balance = income - expense (useful لو عايز تعرض حساب ديناميكي بدل الكاش)
    override fun getComputedFundBalanceFlow(
        fundId: Int,
        startDate: Long,
        endDate: Long,
    ): Flow<Double> {
        return combine(
            getFundIncomeSumFlow(fundId, startDate, endDate),
            getFundExpenseSumFlow(fundId, startDate, endDate)
        ) { inc, exp -> inc - exp }
    }

    // Funds Transfer
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

    //Preferences
    override val themeFlow: Flow<Theme> = dataStore.data.map {
        when (it[Keys.THEME_KEY]) {
            "dark" -> Theme.Dark
            else -> Theme.Light
        }
    }

    override suspend fun saveTheme(theme: Theme) {
        dataStore.edit { it[Keys.THEME_KEY] = theme.value }
    }
}