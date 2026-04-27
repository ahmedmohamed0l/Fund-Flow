package com.axoncodelabs.cashbox.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.axoncodelabs.cashbox.data.local.CashBoxDatabase
import com.axoncodelabs.cashbox.data.local.dao.FundDao
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao
import com.axoncodelabs.cashbox.data.local.migration.MIGRATION_1_2
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.data.repository.CashBoxRepositoryImpl
import com.axoncodelabs.cashbox.data.util.StringProvider
import com.axoncodelabs.cashbox.data.util.StringProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

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
        )
            .addMigrations(MIGRATION_1_2)
            .build()
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
        dataStore: DataStore<Preferences>,
    ): CashBoxRepository = CashBoxRepositoryImpl(fundDao, transactionDao, dataStore)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideStringProvider(
        @ApplicationContext context: Context,
    ): StringProvider = StringProviderImpl(context)
}