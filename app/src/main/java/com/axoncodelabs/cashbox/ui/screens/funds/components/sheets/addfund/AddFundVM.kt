package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addfund

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFundVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var amount by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isNameEmpty by mutableStateOf(false)
        private set

    fun initData() {
        //Clear old
        clearSheetData()
    }

    private val _fundsEvent = Channel<FundsEvent>()
    val fundsEvent = _fundsEvent.receiveAsFlow()

    fun onEvent(event: AddFundEvent) {
        when (event) {
            is AddFundEvent.OnNameChange -> {
                name = event.name
                isNameEmpty = false
            }

            is AddFundEvent.OnAmountChange -> {
                amount = event.amount
            }

            is AddFundEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddFundEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val initialAmount = amount.toDoubleOrNull() ?: 0.0
                    if (name.isBlank()) {
                        isNameEmpty = true
                        /*Disable ShowSnackbar
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = R.string.Sheet_FundNameError
                            )
                        )
                        Log.d("AddFundVM", "hi im VM Snake")
                        */
                        return@launch
                    }

                    val newFund = FundEntity(
                        name = name,
                        balance = 0.0
                    )

                    val fundId = repository.insertFund(newFund).toInt()
                    repository.insertTransaction(
                        TransactionEntity(
                            fundId = fundId,
                            amount = initialAmount,
                            description = description.ifBlank { "القيمة الأولية للصندوق" },
                            type = TransactionType.INCOME
                        )
                    )

                    sendFundsEvent(FundsEvent.CloseSheet)
                }
            }
        }
    }

    fun clearSheetData() {
        name = ""
        amount = ""
        description = ""
        isNameEmpty = false
    }

    private fun sendFundsEvent(event: FundsEvent) {
        viewModelScope.launch {
            _fundsEvent.send(event)
        }
    }
}