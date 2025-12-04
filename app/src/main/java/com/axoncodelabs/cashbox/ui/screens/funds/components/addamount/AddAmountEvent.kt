package com.axoncodelabs.cashbox.ui.screens.funds.components.addamount

sealed class AddAmountEvent {
    data class OnAmountChange(val amount: String) : AddAmountEvent()
    data class OnDescriptionChange(val description: String) : AddAmountEvent()
    object OnSaveClick : AddAmountEvent()
    data class OnDateChange(val newDate: Long) : AddAmountEvent()
}