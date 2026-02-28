package com.axoncodelabs.cashbox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity

@Database(
    entities = [FundEntity::class, TransactionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CashBoxDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
    abstract fun transactionDao(): TransactionDao
}