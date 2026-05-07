package com.axoncodelabs.fundflow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FundFlowApp : Application() {

    @Inject
    lateinit var autoBackupManager: AutoBackupManager

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            autoBackupManager.run()
        }
    }
}