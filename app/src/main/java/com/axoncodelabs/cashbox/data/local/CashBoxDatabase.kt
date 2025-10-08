package com.axoncodelabs.cashbox.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity

@Database(
    entities = [FundEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CashBoxDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: CashBoxDatabase? = null
        fun getInstance(context: Context): CashBoxDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CashBoxDatabase::class.java,
                    "cashbox_db"
                ).build().also { instance = it }
            }
        }
    }
}