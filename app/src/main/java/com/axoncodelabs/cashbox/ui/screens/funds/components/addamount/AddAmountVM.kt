package com.axoncodelabs.cashbox.ui.screens.funds.components.addamount

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.data.util.StringProvider
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAmountVM @Inject constructor(
    private val repository: CashBoxRepository,
    private val stringProvider: StringProvider,
) : ViewModel() {

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
    var selectedDate by mutableLongStateOf(System.currentTimeMillis())
        private set

    fun initData(fund: FundEntity) {
        //Set new
        this.fund = fund
        name = fund.name

        //Clear old
        clearSheetData()
    }

    private val _fundsEvent = Channel<FundsEvent>()
    val fundsEvent = _fundsEvent.receiveAsFlow()

    fun onEvent(event: AddAmountEvent) {
        when (event) {
            is AddAmountEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
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

                    if (description.isBlank()) {
                        description =
                            (stringProvider.getString(R.string.Sheet_AddFundDescription) + "" + name)
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

                    sendFundsEvent(FundsEvent.CloseSheet)
                }
            }
        }
    }

    fun clearSheetData() {
        amount = ""
        description = ""
        isAmountEmpty = false
        selectedDate = System.currentTimeMillis()
    }

    private fun sendFundsEvent(event: FundsEvent) {
        viewModelScope.launch {
            _fundsEvent.send(event)
        }
    }
}