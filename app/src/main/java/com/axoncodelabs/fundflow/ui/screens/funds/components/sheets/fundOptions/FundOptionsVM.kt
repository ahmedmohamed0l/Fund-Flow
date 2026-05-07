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
    var name by mutableStateOf("")
        private set

    var isEditMode by mutableStateOf(true)
        private set
    var isNameEmpty by mutableStateOf(false)
        private set

    var isFundBalanceExcepted by mutableStateOf(false)
        private set

    //──── Helpers ────
    fun clearSheetData() {
        isNameEmpty = false
        isEditMode = false
    }

    //──── Init ────
    fun initData(fund: FundEntity) {
        //Set new
        this.fund = fund
        name = fund.name
        isFundBalanceExcepted = fund.isExcepted

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

            is FundOptionsEvent.OnNameChange -> {
                name = event.name
                isNameEmpty = false
            }

            FundOptionsEvent.OnSaveClick -> {
                viewModelScope.launch {
                    if (name.isBlank()) {
                        isNameEmpty = true
                        return@launch
                    }
                    fund?.let {
                        repository.updateFund(
                            it.copy(
                                name = name
                            )
                        )
                    }
                    isEditMode = false
                }
            }

            FundOptionsEvent.OnExceptFundToggle -> {
                viewModelScope.launch {
                    isFundBalanceExcepted = !isFundBalanceExcepted
                    fund?.let {
                        repository.updateFund(
                            it.copy(
                                isExcepted = isFundBalanceExcepted
                            )
                        )
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