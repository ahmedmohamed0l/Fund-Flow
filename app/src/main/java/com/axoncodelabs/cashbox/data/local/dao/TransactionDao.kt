package com.axoncodelabs.cashbox.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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

    /**...............( Delete All Fund Transactions )...............**/
    /*-----( For Funds_Screen )-----*/
    @Query("SELECT * FROM transactions WHERE fundId = :fundId")
    fun getTransactionsByFundId(fundId: Int): Flow<List<TransactionEntity>>

    @Query("DELETE FROM transactions WHERE fundId = :fundId")
    suspend fun deleteTransactionsByFundId(fundId: Int)

    /**...............( Data Flow )...............**/
    // For (Transaction_Edit)
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    /*-----( For Expenses_Screen )-----*/
    @Query("SELECT * FROM transactions WHERE type = 'EXPENSE' AND isTransfer = 0 AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getExpensesByDateAndType(
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE' AND isTransfer = 0 AND date BETWEEN :startDate AND :endDate")
    fun getExpensesSumByDateAndType(
        startDate: Long,
        endDate: Long,
    ): Flow<Double>
    /*---------------------------------*/

    /*-----( For Reports_Screen )-----*/
    @Query("SELECT * FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getTransactionsByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    fun getTransactionsSumByDateAndType(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>

    @Query("SELECT * FROM transactions WHERE fundId = :fundId And type = :type And date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getTransactionsByFundAndTypeAndDate(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionWithFund>>


    @Query("SELECT SUM(amount) FROM transactions WHERE fundId = :fundId AND type = :type AND date BETWEEN :startDate AND :endDate")
    fun getTransactionsSumByFundAndTypeAndDate(
        fundId: Int,
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>
    /*--------------------------------*/
}
