package com.axoncodelabs.cashbox.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val cashBoxRepository: CashBoxRepository,
) : ViewModel() {

    private val _darkMode = MutableStateFlow<Boolean?>(null)
    val darkMode: StateFlow<Boolean?> = _darkMode.asStateFlow()
    init {
        viewModelScope.launch {
            cashBoxRepository.themeFlow.collect {
                _darkMode.value = it == "dark"
            }
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            cashBoxRepository.saveTheme(if (isDark) "dark" else "light")
        }
    }
}