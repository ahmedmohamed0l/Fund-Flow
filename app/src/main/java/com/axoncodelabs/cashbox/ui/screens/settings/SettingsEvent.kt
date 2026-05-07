package com.axoncodelabs.cashbox.ui.screens.settings

sealed class SettingsEvent {
    data class SheetDisplayed(val sheet: SettingsSheets) : SettingsEvent()
    object CloseSheet : SettingsEvent()

    data class ToggleTheme(val isDark: Boolean) : SettingsEvent()
    data class ToggleHideData(val isHideData: Boolean) : SettingsEvent()
    data class ToggleAutoBackup(val isAutoBackup: Boolean) : SettingsEvent()

    object RefreshBackupList : SettingsEvent()
}