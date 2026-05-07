package com.axoncodelabs.fundflow.ui.screens.funds

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundsViewModel @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {

    //── State ──
    private val _state = MutableStateFlow(FundsState())
    val state = _state.asStateFlow()

    //──── Data Flows ────
    val fundsFlow by mutableStateOf(repository.getAllFunds())

    private val fundsSumFlow = fundsFlow
        .map { list ->
            list
                .filter { !it.isExcepted }
                .sumOf { it.balance }
        }

    //──── Init ────
    init {
        combine(
            fundsFlow,
            fundsSumFlow,
            repository.hideDataFlow
        ) { funds, totalBalance, isHideData ->
            _state.update { currentState ->
                currentState.copy(
                    funds = funds,
                    fundsTotalBalance = totalBalance,
                    isHideData = isHideData
                )
            }
        }.launchIn(viewModelScope)
    }

    //──── Events ────
    fun onEvent(event: FundsEvent) {
        when (event) {
            //── Sheets ──
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

            //── Popups ──
            is FundsEvent.PopupDisplay -> {
                _state.update {
                    it.copy(popupState = event.popup)
                }
            }

            FundsEvent.ClosePopup -> {
                _state.update {
                    it.copy(popupState = FundsPopup.None)
                }
            }

            //── UI ──
            is FundsEvent.DeleteFund -> {
                viewModelScope.launch {
                    repository.deleteFund(event.fund)
                }
                _state.update {
                    it.copy(popupState = FundsPopup.None)
                }
            }
        }
    }
}