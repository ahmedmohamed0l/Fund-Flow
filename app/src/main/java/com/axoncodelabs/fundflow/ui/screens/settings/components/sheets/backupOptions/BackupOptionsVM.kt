package com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.backupOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.ui.components.sheets.SheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupOptionsVM @Inject constructor(
    private val repository: FundFlowRepository
) : ViewModel() {
    //──── Events ────
    private val _endSheetEvent = Channel<SheetResult>(Channel.CONFLATED)
    val endSheetEvent = _endSheetEvent.receiveAsFlow()

    fun onEvent(event: BackupOptionsEvent) {
        when (event) {
            BackupOptionsEvent.CreateBackup -> {
                viewModelScope.launch {
                    repository.createBackup(isAuto = false)
                    _endSheetEvent.send(SheetResult.Added)
                }
            }
        }
    }
}