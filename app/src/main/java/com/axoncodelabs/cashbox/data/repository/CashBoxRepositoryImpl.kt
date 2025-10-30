package com.axoncodelabs.cashbox.data.repository

import androidx.room.Transaction
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CashBoxRepositoryImpl @Inject constructor(
    private val fundDao: FundDao,
    private val transactionDao: TransactionDao,
) : CashBoxRepository {

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

    // Transactions
    override suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
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

        val fromFund = fundDao.getFundById(fromFundId)!!
        val toFund = fundDao.getFundById(toFundId)!!

        fundDao.updateFund(fromFund.copy(balance = fromFund.balance - amount))
        fundDao.updateFund(toFund.copy(balance = toFund.balance + amount))

        transactionDao.insertTransaction(expenseTransaction)
        transactionDao.insertTransaction(incomeTransaction)
    }
}