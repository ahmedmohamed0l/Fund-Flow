package com.axoncodelabs.fundflow.ui.screens.settings

import com.axoncodelabs.fundflow.util.language.Language

sealed class SettingsEvent {
    data class SheetDisplayed(val sheet: SettingsSheets) : SettingsEvent()
    object CloseSheet : SettingsEvent()

    data class ToggleTheme(val isDark: Boolean) : SettingsEvent()
    data class ToggleHideData(val isHideData: Boolean) : SettingsEvent()
    data class SetLanguage(val language: Language) : SettingsEvent()
    data class ToggleAutoBackup(val isAutoBackup: Boolean) : SettingsEvent()

    object RefreshBackupList : SettingsEvent()
}