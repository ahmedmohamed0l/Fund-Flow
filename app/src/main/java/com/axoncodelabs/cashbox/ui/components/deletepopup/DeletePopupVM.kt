package com.axoncodelabs.cashbox.ui.components.deletepopup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletePopupVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
    fun initFund(fund: FundEntity) {
        this.fund = fund
    }

    var fund by mutableStateOf<FundEntity?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DeletePopupEvent) {
        when (event) {
            is DeletePopupEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    fund?.let {
                        repository.deleteFund(it)
                    }
                    sendUiEvent(UiEvent.ClosePopup)
                }
            }

            is DeletePopupEvent.OnCancelClick -> {
                sendUiEvent(UiEvent.ClosePopup)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}