package com.axoncodelabs.cashbox

import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AutoBackupManager @Inject constructor(
    private val repository: CashBoxRepository
) {
    suspend fun run() {
        val enabled = repository.autoBackupFlow.first()
        if (enabled) {
            repository.createBackup()
        }
    }
}