package com.axoncodelabs.fundflow

import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AutoBackupManager @Inject constructor(
    private val repository: FundFlowRepository
) {
    suspend fun run() {
        val enabled = repository.autoBackupFlow.first()
        if (enabled) {
            repository.createBackup()
        }
    }
}