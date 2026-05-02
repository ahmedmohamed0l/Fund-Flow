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

    //──── Init ────
    init {
        combine(
            repository.themeFlow,
            repository.hideDataFlow
        ) { theme, isHideData ->
            _state.update { currentState ->
                currentState.copy(
                    darkMode = theme is Theme.Dark,
                    isHideData = isHideData
                )
            }
        }.launchIn(viewModelScope)
    }

    //──── Events ────
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleTheme -> {
                viewModelScope.launch {
                    repository.saveTheme(
                        if (event.isDark) Theme.Dark else Theme.Light
                    )
                }
            }

            is SettingsEvent.ToggleHideData -> {
                viewModelScope.launch {
                    repository.saveHideData(event.isHideData)
                }
            }
        }
    }
}