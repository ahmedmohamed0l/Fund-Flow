package com.axoncodelabs.fundflow

import android.app.Application
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.util.language.AppLanguageManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class FundFlowApp : Application() {

    @Inject
    lateinit var autoBackupManager: AutoBackupManager

    @Inject
    lateinit var repository: FundFlowRepository

    @Inject
    lateinit var appLanguageManager: AppLanguageManager

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            autoBackupManager.run()
        }

        appLanguageManager.initialize(
            runBlocking(Dispatchers.IO) {
                repository.languageFlow.first()
            }
        )

    }
}