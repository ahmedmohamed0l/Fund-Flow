package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.fundOptions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundOptionsVM @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {

    //──── UI State ────
    var fund by mutableStateOf<FundEntity?>(null)
        private set

    var isEditMode by mutableStateOf(true)
        private set

    //──── Helpers ────
    fun clearSheetData() {
        isEditMode = false
    }

    //──── Init ────
    fun initData(fund: FundEntity) {
        //Set new
        this.fund = fund

        //Clear old
        clearSheetData()
    }

    //──── Events ────
    private val _closeEvent = Channel<Unit>(Channel.CONFLATED)
    val closeEvent = _closeEvent.receiveAsFlow()

    fun onEvent(event: FundOptionsEvent) {
        when (event) {
            FundOptionsEvent.OnEditFundClick -> {
                isEditMode = true
            }

            is FundOptionsEvent.OnSaveClick -> {
                viewModelScope.launch {
                    if (event.name.isBlank()) {
                        return@launch
                    }
                    fund?.let {
                        val updatedFund = it.copy(name = event.name)
                        repository.updateFund(updatedFund)
                        fund = updatedFund
                    }
                    isEditMode = false
                }
            }

            is FundOptionsEvent.OnExceptFundToggle -> {
                viewModelScope.launch {
                    fund?.let {
                        val updatedFund = it.copy(isExcepted = event.isExcepted)
                        repository.updateFund(updatedFund)
                        fund = updatedFund
                    }
                }
            }

            // ────────( Delete Fund Transactions Popup events )────────
            FundOptionsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    fund?.let {
                        repository.deleteAllFundTransactions(it.id)
                    }
                    _closeEvent.send(Unit)
                }
            }

            FundOptionsEvent.OnCancelClick -> {
                viewModelScope.launch {
                    _closeEvent.send(Unit)
                }
            }
        }
    }
}