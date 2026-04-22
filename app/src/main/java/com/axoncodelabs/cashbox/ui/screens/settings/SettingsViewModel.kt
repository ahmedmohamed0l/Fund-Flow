package com.axoncodelabs.cashbox.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.themeFlow.collect { theme ->
                _state.value = _state.value.copy(darkMode = theme is Theme.Dark)
            }

        }

        viewModelScope.launch {
            repository.hideDataFlow.collect { isHideData ->
                _state.value = _state.value.copy(isHideData = isHideData)
            }
        }
    }

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