package com.axoncodelabs.cashbox.ui.screens.funds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FundsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FundsState())
    val state = _state.asStateFlow()

    init {
        combine(
            repository.getAllFunds(),
            repository.getFundsSUM(),
            repository.hideDataFlow
        ) { funds, totalBalance, isHideData ->
            _state.update {
                it.copy(
                    funds = funds,
                    fundsTotalBalance = totalBalance,
                    isHideData = isHideData
                )
            }
        }.launchIn(viewModelScope)
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