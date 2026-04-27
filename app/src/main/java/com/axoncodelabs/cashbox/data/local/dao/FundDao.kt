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
    @Insert
    suspend fun insertFund(fund: FundEntity): Long

    @Update
    suspend fun updateFund(fund: FundEntity)

    @Delete
    suspend fun deleteFund(fund: FundEntity)

    //────────────────{ DATA FLOW }────────────────

    @Query("SELECT * FROM funds WHERE id = :id")
    suspend fun getFundById(id: Int): FundEntity?

    @Query("SELECT * FROM funds ORDER BY createdAt ASC")
    fun getAllFunds(): Flow<List<FundEntity>>
}