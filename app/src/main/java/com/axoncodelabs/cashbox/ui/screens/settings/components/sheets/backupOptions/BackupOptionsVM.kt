package com.axoncodelabs.cashbox.ui.screens.settings.components.sheets.backupOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupOptionsVM @Inject constructor(
    private val repository: CashBoxRepository
) : ViewModel() {
    //──── Events ────
    private val _closeEvent = Channel<Unit>(Channel.CONFLATED)
    val closeEvent = _closeEvent.receiveAsFlow()

    fun onEvent(event: BackupOptionsEvent) {
        when (event) {
            BackupOptionsEvent.CreateBackup -> {
                viewModelScope.launch {
                    repository.createBackup()
                    _closeEvent.send(Unit)
                }
            }
        }
    }
}