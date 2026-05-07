package com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.backupOptions

sealed class BackupOptionsEvent {
    object CreateBackup : BackupOptionsEvent()
}