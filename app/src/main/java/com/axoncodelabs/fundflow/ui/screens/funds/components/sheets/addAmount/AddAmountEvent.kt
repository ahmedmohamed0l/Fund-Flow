package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.addAmount

sealed class AddAmountEvent {
    data class OnAmountChange(val amount: String) : AddAmountEvent()
    data class OnDescriptionChange(val description: String) : AddAmountEvent()
    data class OnDateChange(val newDate: Long) : AddAmountEvent()
    object OnSaveClick : AddAmountEvent()
}