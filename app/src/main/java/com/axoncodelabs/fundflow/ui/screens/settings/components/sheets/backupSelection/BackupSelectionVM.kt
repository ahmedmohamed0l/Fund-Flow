package com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.backupSelection

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
class BackupSelectionVM @Inject constructor(
    private val repository: FundFlowRepository
) : ViewModel() {
    //──── Events ────
    private val _endSheetEvent = Channel<SheetResult>(Channel.CONFLATED)
    val endSheetEvent = _endSheetEvent.receiveAsFlow()

    fun onEvent(event: BackupSelectionEvent) {
        when (event) {
            is BackupSelectionEvent.OnSelectBackup -> {
                viewModelScope.launch {
                    repository.restoreBackup(event.fileName)
                    _endSheetEvent.send(SheetResult.Updated)
                }
            }

            is BackupSelectionEvent.OnDeleteBackup -> {
                viewModelScope.launch {
                    repository.deleteBackup(event.fileName)
                    _endSheetEvent.send(SheetResult.Deleted)
                }
            }
        }
    }
}