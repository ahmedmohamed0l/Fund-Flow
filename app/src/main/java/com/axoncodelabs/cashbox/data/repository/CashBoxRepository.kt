package com.axoncodelabs.cashbox.data.repository

import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CashBoxRepository {
    // Funds
    suspend fun insertFund(fund: FundEntity)
    suspend fun updateFund(fund: FundEntity)
    suspend fun deleteFund(fund: FundEntity)
    fun getAllFunds(): Flow<List<FundEntity>>
    suspend fun getFundById(id: Int): FundEntity?
    fun getTotalBalance(): Flow<Double>

    // Transactions
    suspend fun insertTransaction(transaction: TransactionEntity)
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)
    suspend fun getTransactionById(id: Int): TransactionEntity?
    fun getTransactionsByDate(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>
    fun getTransactionsByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>
    fun getTransactionsSumByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>
    /* I'll not use it
        fun getTransactionsByFundAndDate(fundId: Int, startDate: Long, endDate: Long) : Flow<List<TransactionEntity>>*/

    // Funds Transfer
    suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long = System.currentTimeMillis(),
    )
}