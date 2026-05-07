package com.axoncodelabs.fundflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionType
import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    //──── Backup & Restore ────
    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactionsList(): List<TransactionEntity>

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    //──── For (Funds_Screen) ────
    @Query("DELETE FROM transactions WHERE fundId = :fundId")
    suspend fun deleteAllTransactionsByFundId(fundId: Int)

    // ────────────────{ DATA FLOW }────────────────
    //──── For (Transaction_Edit) ────
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    //──── For (Expenses_Screen) ────
    @Transaction
    @Query("SELECT * FROM transactions WHERE type = 'EXPENSE' AND isTransfer = 0 AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getExpensesByDateAndType(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    //──── For (Reports_Screen) ────
    data class DateRange(val firstDate: Long?, val lastDate: Long?)

    @Query("SELECT MIN(date) AS firstDate, MAX(date) AS lastDate FROM transactions")
    fun getFirstAndLastDate(): Flow<DateRange>

    @Query("SELECT MAX(date) FROM transactions GROUP BY strftime('%Y-%m', date / 1000, 'unixepoch') ORDER BY date DESC")
    fun getAllDates(): Flow<List<Long>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getTransactionsByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE fundId = :fundId And type = :type And date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getTransactionsByFundAndDateAndType(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>
}
