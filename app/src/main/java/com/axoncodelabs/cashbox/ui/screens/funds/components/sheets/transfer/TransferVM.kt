package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.transfer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.data.util.MyStringProvider
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TransferVM @Inject constructor(
    private val repository: CashBoxRepository,
    private val myStringProvider: MyStringProvider,
) : ViewModel() {
    var fromFund by mutableStateOf<FundEntity?>(null)
        private set
    var fromName by mutableStateOf("")
        private set
    var fromBalance by mutableDoubleStateOf(0.0)
        private set

    var toFund by mutableStateOf<FundEntity?>(null)
        private set
    var toName by mutableStateOf("")
        private set
    var isNoFundSelected by mutableStateOf(false)
        private set

    var availableBalance by mutableDoubleStateOf(0.00)
        private set

    var isAvailableNegative by mutableStateOf(false)
        private set


    var amount by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isAmountEmpty by mutableStateOf(false)
        private set
    var selectedDate by mutableLongStateOf(System.currentTimeMillis())
        private set

    private fun updateAvailableBalance() {
        val amount = amount.toDoubleOrNull() ?: 0.0
        val delta = fromBalance - amount
        availableBalance = delta
        isAvailableNegative = delta < 0
    }

    fun initTransaction(fundFrom: FundEntity) {
        this.fromFund = fundFrom
        fromName = fundFrom.name
        fromBalance = fundFrom.balance
        clearSheetData()
    }


    private val _fundsEvent = Channel<FundsEvent>()
    val fundsEvent = _fundsEvent.receiveAsFlow()

    fun onEvent(event: TransferEvent) {
        when (event) {
            is TransferEvent.OnToFundSelected -> {
                toFund = event.fund
                toName = event.fund.name
                isNoFundSelected = false
            }

            is TransferEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
                updateAvailableBalance()
            }

            is TransferEvent.OnDescriptionChange -> {
                description = event.description
            }

            is TransferEvent.OnDateChange -> {
                selectedDate = event.newDate
            }

            TransferEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val source = fromFund!!
                    val target = toFund ?: run {
                        isNoFundSelected = true
                        return@launch
                    }

                    val amountDouble = amount.toDoubleOrNull()
                    if (amount.isBlank() || amountDouble == null) {
                        isAmountEmpty = true
                        return@launch
                    }

                    if (description.isBlank()) {
                        description =
                            (myStringProvider.getString(R.string.Sheet_TransferFromDescription)) + " " + source.name +
                                    (myStringProvider.getString(R.string.Sheet_TransferToDescription)) + " " + target.name
                    }

                    repository.transferBetweenFunds(
                        fromFundId = source.id,
                        toFundId = target.id,
                        amount = amountDouble,
                        description = description,
                        timestamp = selectedDate
                    )

                    clearSheetData()
                    sendFundsEvent(FundsEvent.CloseSheet)
                }
            }
        }
    }

    fun clearSheetData() {
        toFund = null
        toName = ""
        isNoFundSelected = false
        amount = ""
        description = ""
        isAmountEmpty = false
        selectedDate = System.currentTimeMillis()
        updateAvailableBalance()
    }

    private fun sendFundsEvent(event: FundsEvent) {
        viewModelScope.launch {
            _fundsEvent.send(event)
        }
    }
}