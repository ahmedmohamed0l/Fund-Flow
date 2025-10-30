package com.axoncodelabs.cashbox.data.di

import android.app.Application
import androidx.room.Room
import com.axoncodelabs.cashbox.data.local.CashBoxDatabase
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.data.repository.CashBoxRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCashBoxDatabase(app: Application): CashBoxDatabase {
        return Room.databaseBuilder(
            app,
            CashBoxDatabase::class.java,
            "cashbox_db"
        ).build()
    }

    @Provides
    fun provideFundDao(db: CashBoxDatabase): FundDao {
        return db.fundDao()
    }

    @Provides
    fun provideTransactionDao(db: CashBoxDatabase): TransactionDao {
        return db.transactionDao()
    }

    @Provides
    @Singleton
    fun provideRepository(
        fundDao: FundDao,
        transactionDao: TransactionDao,
    ): CashBoxRepository = CashBoxRepositoryImpl(fundDao, transactionDao)
}