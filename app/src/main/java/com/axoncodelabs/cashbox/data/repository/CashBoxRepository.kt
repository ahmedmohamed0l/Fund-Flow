package com.axoncodelabs.cashbox.data.repository

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.ui.theme.Theme
import kotlinx.coroutines.flow.Flow

interface CashBoxRepository {
    // Funds
    suspend fun insertFund(fund: FundEntity): Long
    suspend fun updateFund(fund: FundEntity)
    suspend fun deleteFund(fund: FundEntity)
    suspend fun deleteAllFundTransactions(fundId: Int)
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

    fun getExpensesByDate(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>

    // داخل interface CashBoxRepository
    fun getFundIncomeSumFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double>
    fun getFundExpenseSumFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double>
    fun getComputedFundBalanceFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double>


    // Funds Transfer
    suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long = System.currentTimeMillis(),
    )

    //-----------------[ Preferences ]-----------------

    //......( Read Flow )......
    val themeFlow: Flow<Theme>
    val hideDataFlow: Flow<Boolean>

    //......( Save )......
    suspend fun saveTheme(theme: Theme)
    suspend fun saveHideData(isHide: Boolean)
}