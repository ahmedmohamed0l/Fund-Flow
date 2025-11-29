package com.axoncodelabs.cashbox.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FundDao {
    @Query("SELECT * FROM funds WHERE name = :name LIMIT 1")
    suspend fun getFundByName(name: String): FundEntity?

    @Insert
    suspend fun insertFund(fund: FundEntity)

    @Update
    suspend fun updateFund(fund: FundEntity)

    @Delete
    suspend fun deleteFund(fund: FundEntity)

    @Query("SELECT * FROM funds ORDER BY createdAt DESC")
    fun getAllFunds(): Flow<List<FundEntity>>

    @Query("SELECT * FROM funds WHERE id = :id")
    suspend fun getFundById(id: Int): FundEntity?

    @Query("SELECT SUM(balance) FROM funds")
    fun getTotalBalance(): Flow<Double>
}