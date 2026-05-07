package com.axoncodelabs.cashbox.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    //── State ──
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    //── Helpers ──
    fun refreshBackups() {
        viewModelScope.launch {
            _state.update {
                it.copy(backupList = repository.getAvailableBackups())
            }
        }
    }

    //──── Init ────
    init {
        refreshBackups()
        combine(
            repository.themeFlow,
            repository.hideDataFlow,
            repository.lastBackupDateFlow,
            repository.autoBackupFlow
        ) { theme, isHideData, lastBackup, isAutoBackup ->
            _state.update { currentState ->
                currentState.copy(
                    darkMode = theme is Theme.Dark,
                    isHideData = isHideData,
                    lastBackupDate = lastBackup,
                    isAutoBackup = isAutoBackup
                )
            }
        }.launchIn(viewModelScope)
    }

    //──── Events ────
    fun onEvent(event: SettingsEvent) {
        when (event) {
            //── Sheets ──
            is SettingsEvent.SheetDisplayed -> {
                _state.update {
                    it.copy(currentSheet = event.sheet)
                }
            }

            SettingsEvent.CloseSheet -> {
                _state.update {
                    it.copy(currentSheet = SettingsSheets.None)
                }
            }

            //── Theme Toggle ──
            is SettingsEvent.ToggleTheme -> {
                viewModelScope.launch {
                    repository.saveTheme(
                        if (event.isDark) Theme.Dark else Theme.Light
                    )
                }
            }

            //── HideData Toggle ──
            is SettingsEvent.ToggleHideData -> {
                viewModelScope.launch {
                    repository.saveHideData(event.isHideData)
                }
            }

            //── AutoBackup Toggle ──
            is SettingsEvent.ToggleAutoBackup -> {
                viewModelScope.launch {
                    repository.saveAutoBackup(event.isAutoBackup)
                }
            }

            SettingsEvent.RefreshBackupList -> {

            }
        }
    }
}