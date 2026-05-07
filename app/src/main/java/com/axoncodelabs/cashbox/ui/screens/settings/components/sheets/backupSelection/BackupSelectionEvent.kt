package com.axoncodelabs.cashbox.ui.screens.settings.components.sheets.backupSelection

sealed class BackupSelectionEvent {
    data class OnSelectBackup(val fileName: String) : BackupSelectionEvent()
    data class OnDeleteBackup(val fileName: String) : BackupSelectionEvent()
}