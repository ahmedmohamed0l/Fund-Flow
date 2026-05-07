package com.axoncodelabs.cashbox.ui.screens.settings.components.sheets.backupOptions

sealed class BackupOptionsEvent {
    object CreateBackup : BackupOptionsEvent()
}