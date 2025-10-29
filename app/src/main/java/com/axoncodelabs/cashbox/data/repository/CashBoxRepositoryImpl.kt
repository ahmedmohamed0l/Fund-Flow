package com.axoncodelabs.cashbox.data.repository

import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import javax.inject.Inject


class CashBoxRepositoryImpl @Inject constructor(
    private val fundDao: FundDao,
    private val transactionDao: TransactionDao,
) : CashBoxRepository {
    // Funds
    override suspend fun insertFund(fund: FundEntity) = fundDao.insertFund(fund)
    override suspend fun updateFund(fund: FundEntity) = fundDao.updateFund(fund)
    override suspend fun deleteFund(fund: FundEntity) = fundDao.deleteFund(fund)
    override fun getAllFunds() = fundDao.getAllFunds()
    override suspend fun getFundById(id: Int) = fundDao.getFundById(id)
    override fun getTotalBalance() = fundDao.getTotalBalance()

    // Transactions
    override suspend fun insertTransaction(transaction: TransactionEntity) =
        transactionDao.insertTransaction(transaction)
    override suspend fun updateTransaction(transaction: TransactionEntity) =
        transactionDao.updateTransaction(transaction)
    override suspend fun deleteTransaction(transaction: TransactionEntity) =
        transactionDao.deleteTransaction(transaction)
    override suspend fun getTransactionById(id: Int) = transactionDao.getTransactionById(id)
    override fun getTransactionsByDate(startDate: Long, endDate: Long) =
        transactionDao.getTransactionsByDate(startDate, endDate)
    override fun getTransactionsSumByType(fundId: Int, type: TransactionType, startDate: Long, endDate: Long ) = transactionDao.getTransactionsSumByType(fundId, type, startDate, endDate)
    override fun getTransactionsByType(fundId: Int, type: TransactionType, startDate: Long, endDate: Long) =
        transactionDao.getTransactionsByType(fundId, type, startDate, endDate)
    /* I'll not use it
        override fun getTransactionsByFundAndDate(fundId: Int, startDate: Long, endDate: Long) = transactionDao.getTransactionsByFundAndDate(fundId, startDate, endDate)*/


    //TODO CHECKPOINT: "ChatGPT" (TodoRepository (Interface + Implementation) -> CashBoxRepositoryImpl | Than edit CashBoxModule)

    // Funds Transfer
    override suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long
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

//class CashBoxRepositoryImpl {}