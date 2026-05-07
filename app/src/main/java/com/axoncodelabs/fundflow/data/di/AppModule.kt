package com.axoncodelabs.fundflow.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.axoncodelabs.fundflow.data.local.FundFlowDatabase
import com.axoncodelabs.fundflow.data.local.dao.FundDao
import com.axoncodelabs.fundflow.data.local.dao.TransactionDao
import com.axoncodelabs.fundflow.data.local.migration.MIGRATION_1_2
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.data.repository.FundFlowRepositoryImpl
import com.axoncodelabs.fundflow.data.util.StringProvider
import com.axoncodelabs.fundflow.data.util.StringProviderImpl
import com.axoncodelabs.fundflow.data.util.backup.BackupFileManager
import com.axoncodelabs.fundflow.data.util.backup.BackupSerializer
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
    fun provideFundFlowDatabase(app: Application): FundFlowDatabase {
        return Room.databaseBuilder(
            app,
            FundFlowDatabase::class.java,
            "fundflow_db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideFundDao(db: FundFlowDatabase): FundDao = db.fundDao()

    @Provides
    fun provideTransactionDao(db: FundFlowDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun provideRepository(
        db: FundFlowDatabase,
        fundDao: FundDao,
        transactionDao: TransactionDao,
        dataStore: DataStore<Preferences>,
        backupFileManager: BackupFileManager,
        backupSerializer: BackupSerializer
    ): FundFlowRepository = FundFlowRepositoryImpl(
        db, fundDao, transactionDao, dataStore, backupFileManager, backupSerializer
    )

    @Provides
    @Singleton
    fun provideBackupFileManager(@ApplicationContext context: Context): BackupFileManager =
        BackupFileManager(context)

    @Provides
    @Singleton
    fun provideBackupSerializer(): BackupSerializer = BackupSerializer()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider =
        StringProviderImpl(context)
}