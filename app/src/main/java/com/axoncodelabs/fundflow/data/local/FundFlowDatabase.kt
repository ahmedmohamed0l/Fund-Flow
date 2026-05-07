package com.axoncodelabs.fundflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.axoncodelabs.fundflow.data.local.dao.FundDao
import com.axoncodelabs.fundflow.data.local.dao.TransactionDao
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity

@Database(
    entities = [FundEntity::class, TransactionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FundFlowDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
    abstract fun transactionDao(): TransactionDao
}