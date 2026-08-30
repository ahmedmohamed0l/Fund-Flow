package com.axoncodelabs.fundflow.ui.screens.settings

import com.axoncodelabs.fundflow.data.util.backup.BackupInfo
import com.axoncodelabs.fundflow.util.language.Language

sealed class SettingsSheets {
    object None : SettingsSheets()
    data class LanguageChanger(var currentLanguage: Language) : SettingsSheets()
    data class BackupOptions(var isAutoBackup: Boolean) : SettingsSheets()
    object BackupSelection : SettingsSheets()
}

data class SettingsState(
    val currentSheet: SettingsSheets = SettingsSheets.None,

    val darkMode: Boolean? = null,
    val isHideData: Boolean = false,

    val currentLanguage: Language? = null,

    val lastBackupDate: Long? = null,
    val isAutoBackup: Boolean? = null,

    val backupList: List<BackupInfo> = emptyList(),
)