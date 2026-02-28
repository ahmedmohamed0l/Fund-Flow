package com.axoncodelabs.cashbox.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    //-----------------[ Delete Transactions By FundId ]-----------------
    @Query("SELECT * FROM transactions WHERE fundId = :fundId")
    fun getTransactionsByFundId(fundId: Int): Flow<List<TransactionEntity>>

    @Query("DELETE FROM transactions WHERE fundId = :fundId")
    suspend fun deleteTransactionsByFundId(fundId: Int)
    //-------------------------------------------------------------------

    @Query("SELECT * FROM transactions WHERE type = 'EXPENSE' And date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getExpensesByDate(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE' And date BETWEEN :startDate AND :endDate")
    fun getExpensesSumByDate(
        startDate: Long,
        endDate: Long,
    ): Flow<Double>

    @Query("SELECT * FROM transactions WHERE fundId = :fundId And type = :type And date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getTransactionsByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE fundId = :fundId AND type = :type AND date BETWEEN :startDate AND :endDate")
    fun getTransactionsSumByType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>

    @Transaction
    @Query(
        """
    SELECT * FROM transactions
    WHERE type = 'EXPENSE'
    AND isTransfer = 0
    AND date BETWEEN :startDate AND :endDate
    ORDER BY date ASC
"""
    )
    fun getExpensesWithFundByDate(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithFund>>
}
