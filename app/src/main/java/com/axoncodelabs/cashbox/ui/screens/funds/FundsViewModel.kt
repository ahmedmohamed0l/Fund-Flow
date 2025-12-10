package com.axoncodelabs.cashbox.ui.screens.funds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    val funds = repository.getAllFunds()
    val fundsTotalBalance = repository.getTotalBalance()

    private val _state = MutableStateFlow(FundsState())
    val state: StateFlow<FundsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.hideDataFlow.collect { isHideData ->
                _state.value = _state.value.copy(isHideData = isHideData)
            }
        }
    }

    fun onEvent(event: FundsEvent) {
        when (event) {
            is FundsEvent.SheetDisplayed -> {
                _state.update {
                    it.copy(currentSheet = event.sheet)
                }
            }

            FundsEvent.CloseSheet -> {
                _state.update {
                    it.copy(currentSheet = FundsSheets.None)
                }
            }

            is FundsEvent.PopupDisplay -> {
                _state.update {
                    it.copy(popupState = event.popup)
                }
            }

            FundsEvent.ClosePopup -> {
                _state.update {
                    it.copy(popupState = FundsPopup.Close)
                }
            }
        }
    }
}