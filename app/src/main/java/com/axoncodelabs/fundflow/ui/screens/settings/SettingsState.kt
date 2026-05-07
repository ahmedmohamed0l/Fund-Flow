package com.axoncodelabs.fundflow.ui.screens.settings

import com.axoncodelabs.fundflow.data.util.backup.BackupInfo

sealed class SettingsSheets {
    object None : SettingsSheets()
    data class BackupOptions(var isAutoBackup: Boolean) : SettingsSheets()
    object BackupSelection : SettingsSheets()
}

data class SettingsState(
    val currentSheet: SettingsSheets = SettingsSheets.None,

    val darkMode: Boolean? = null,
    val isHideData: Boolean = false,

    val lastBackupDate: Long? = null,
    val isAutoBackup: Boolean? = null,

    val backupList: List<BackupInfo> = emptyList(),
)