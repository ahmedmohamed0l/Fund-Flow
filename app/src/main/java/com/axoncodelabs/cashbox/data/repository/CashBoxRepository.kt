package com.axoncodelabs.cashbox.data.repository

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.ui.theme.Theme
import kotlinx.coroutines.flow.Flow

interface CashBoxRepository {
    /**...............( Fund Actions )...............**/
    suspend fun insertFund(fund: FundEntity): Long
    suspend fun updateFund(fund: FundEntity)
    suspend fun deleteFund(fund: FundEntity)

    // For All App Sheets
    suspend fun getFundById(id: Int): FundEntity?

    /*-----( For Funds_Screen )-----*/
    fun getAllFunds(): Flow<List<FundEntity>>
    fun getFundsSUM(): Flow<Double>

    suspend fun deleteAllFundTransactions(fundId: Int)
    /*------------------------------*/

    /**...............( Transaction Actions )...............**/
    suspend fun insertTransaction(transaction: TransactionEntity)
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // For (Transaction_Edit)
    suspend fun getTransactionById(id: Int): TransactionEntity?

    /*-----( For Expenses_Screen )-----*/
    fun getExpensesByDateAndType(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    fun getExpensesSumByDateAndType(
        startDate: Long,
        endDate: Long,
    ): Flow<Double>
    /*---------------------------------*/

    /*-----( For Reports_Screen )-----*/
    fun getAllDates(): Flow<List<Long>>

    fun getTransactionsByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    fun getTransactionsSumByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>

    fun getTransactionsByFundAndTypeAndDate(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    fun getTransactionsSumByFundAndTypeAndDate(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>
    /*--------------------------------*/

    /**...............( Funds Transfer )...............**/
    suspend fun transferBetweenFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
        timestamp: Long = System.currentTimeMillis(),
    )

    /*
//    fun getExpensesByDate(
//        startDate: Long,
//        endDate: Long,
//    ): Flow<List<TransactionEntity>>

//    fun getAllTransactionsByFundAndDate(
//        fundId: Int,
//        startDate: Long,
//        endDate: Long,
//    ): Flow<List<TransactionEntity>>



//    fun getTransactionsByType(
//        fundId: Int,
//        type: TransactionType,
//        startDate: Long,
//        endDate: Long,
//    ): Flow<List<TransactionEntity>>

//    fun getTransactionsSumByType(
//        fundId: Int,
//        type: TransactionType,
//        startDate: Long,
//        endDate: Long,
//    ): Flow<Double>

//    fun getFundIncomeSumFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double>
//    fun getFundExpenseSumFlow(fundId: Int, startDate: Long, endDate: Long): Flow<Double>

     */

    /**...............( Preferences )...............**/

    //......( Read Flow )......
    val themeFlow: Flow<Theme>
    val hideDataFlow: Flow<Boolean>

    //......( Save )......
    suspend fun saveTheme(theme: Theme)
    suspend fun saveHideData(isHide: Boolean)
}