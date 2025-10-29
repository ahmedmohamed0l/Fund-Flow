package com.axoncodelabs.cashbox.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
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

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDate(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE fundId = :fundId And type = :type And date BETWEEN :startDate AND :endDate ORDER BY date DESC")
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

    /* I'll not use it
    @Query("SELECT * FROM transactions WHERE fundId = :fundId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByFundAndDate(
        fundId: Int,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>*/
}