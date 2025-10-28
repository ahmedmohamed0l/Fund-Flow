package com.axoncodelabs.cashbox.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.axoncodelabs.cashbox.data.local.CashBoxDatabase
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCashBoxDatabase(app : Application): CashBoxDatabase {
        return Room.databaseBuilder(
            app,
            CashBoxDatabase::class.java,
            "cashbox_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFundDao(database: CashBoxDatabase): FundDao {
        return database.fundDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: CashBoxDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideRepository(
        fundDao: FundDao,
        transactionDao: TransactionDao,
    ): CashBoxRepository = CashBoxRepository(fundDao, transactionDao)
}