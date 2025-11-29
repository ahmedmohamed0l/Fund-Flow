package com.axoncodelabs.cashbox.ui.screens.settings

sealed class SettingsEvent {
    data class ToggleTheme(val isDark: Boolean) : SettingsEvent()
}