package com.axoncodelabs.cashbox.ui.screens.funds.components.addfund

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.UiEvent
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
    var isAmountEmpty by mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddFundEvent) {
        when (event) {
            is AddFundEvent.OnNameChange -> {
                name = event.name
                isNameEmpty = false
            }

            is AddFundEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
            }

            is AddFundEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddFundEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
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
                    repository.insertFund(
                        FundEntity(
                            name = name,
                            balance = amountDouble,
                            description = description
                        )
                    )
                    name = ""
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