package com.axoncodelabs.cashbox.data.repository

import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import javax.inject.Inject

class CashBoxRepository @Inject constructor(
    private val fundDao: FundDao,
    private val transactionDao: TransactionDao,
) {
    // Funds
    suspend fun insertFund(fund: FundEntity) = fundDao.insertFund(fund)
    suspend fun updateFund(fund: FundEntity) = fundDao.updateFund(fund)
    suspend fun deleteFund(fund: FundEntity) = fundDao.deleteFund(fund)
    fun getAllFunds() = fundDao.getAllFunds()
    suspend fun getFundById(id: Int) = fundDao.getFundById(id)
    fun getTotalBalance() = fundDao.getTotalBalance()

    // Transactions
    suspend fun insertTransaction(transaction: TransactionEntity) =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: TransactionEntity) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: TransactionEntity) =
        transactionDao.deleteTransaction(transaction)

    fun getTransactionsByDate(startDate: Long, endDate: Long) =
        transactionDao.getTransactionsByDate(startDate, endDate)

    fun getTransactionsByFundAndDate(fundId: Int, startDate: Long, endDate: Long) =
        transactionDao.getTransactionsByFundAndDate(fundId, startDate, endDate)

    fun getTransactionsByType(fundId: Int, type: TransactionType, startDate: Long, endDate: Long) =
        transactionDao.getTransactionsByType(fundId, type, startDate, endDate)

    fun getTransactionsSumByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ) = transactionDao.getTransactionsSumByType(fundId, type, startDate, endDate)

    // Funds Transfer
    suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long = System.currentTimeMillis(),
    ) {
        val expenseTransaction = TransactionEntity(
            amount = amount,
            description = description,
            date = timestamp,
            type = TransactionType.EXPENSE,
            fundId = fromFundId,
            isTransfer = true
        )
        val incomeTransaction = TransactionEntity(
            amount = amount,
            description = description,
            date = timestamp,
            type = TransactionType.INCOME,
            fundId = toFundId,
            isTransfer = true
        )

        val fromFund = fundDao.getFundById(fromFundId)!!
        val toFund = fundDao.getFundById(toFundId)!!

        fundDao.updateFund(fromFund.copy(balance = fromFund.balance - amount))
        fundDao.updateFund(toFund.copy(balance = toFund.balance + amount))
        transactionDao.insertTransaction(expenseTransaction)
        transactionDao.insertTransaction(incomeTransaction)
    }
}