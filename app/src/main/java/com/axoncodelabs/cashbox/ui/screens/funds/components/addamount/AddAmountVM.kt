package com.axoncodelabs.cashbox.ui.screens.funds.components.addamount

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAmountVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    fun initFund(fund: FundEntity) {
        this.fund = fund
        name = fund.name
    }

    var fund by mutableStateOf<FundEntity?>(null)
        private set

    var name by mutableStateOf("")
        private set
    var amount by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isAmountEmpty by mutableStateOf(false)
        private set
    var isAmountNegative by mutableStateOf(false)
        private set
    var selectedDate by mutableStateOf(System.currentTimeMillis())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddAmountEvent) {
        when (event) {
            is AddAmountEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
                isAmountNegative = false
            }

            is AddAmountEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddAmountEvent.OnDateChange -> {
                selectedDate = event.newDate
            }

            is AddAmountEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val amountDouble = amount.toDoubleOrNull()
                    if (amount.isBlank() || amountDouble == null) {
                        isAmountEmpty = true
                        return@launch
                    }
                    if (amountDouble < 0) {
                        isAmountNegative = true
                        return@launch
                    }
                    fund?.let {
                        repository.insertTransaction(
                            TransactionEntity(
                                fundId = it.id,
                                amount = amountDouble,
                                description = description,
                                type = TransactionType.INCOME,
                                date = selectedDate
                            )
                        )
                    }
                    amount = ""
                    description = ""
                    sendUiEvent(UiEvent.CloseSheet)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}