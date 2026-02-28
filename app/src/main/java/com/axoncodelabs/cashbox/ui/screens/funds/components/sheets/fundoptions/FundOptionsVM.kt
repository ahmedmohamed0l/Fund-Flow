package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.fundoptions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FundOptionsVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

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

    var showDeleteFundTransPopup by mutableStateOf(false)

    fun initData(fund: FundEntity) {
        //Set new
        this.fund = fund
        name = fund.name
        isFundBalanceExcepted = fund.isExcepted

        //Clear old
        clearSheetData()
    }

    private val _fundsEvent = Channel<FundsEvent>()
    val fundsEvent = _fundsEvent.receiveAsFlow()

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

            //Delete Fund Transactions Popup events
            FundOptionsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    fund?.let {
                        repository.deleteAllFundTransactions(it.id)
                    }
                    sendFundsEvent(FundsEvent.ClosePopup)
                    sendFundsEvent(FundsEvent.CloseSheet)
                }
            }

            FundOptionsEvent.OnCancelClick -> {
                sendFundsEvent(FundsEvent.ClosePopup)
            }
        }
    }

    fun clearSheetData() {
        isNameEmpty = false
        isEditMode = false
    }

    private fun sendFundsEvent(event: FundsEvent) {
        viewModelScope.launch {
            _fundsEvent.send(event)
        }
    }
}