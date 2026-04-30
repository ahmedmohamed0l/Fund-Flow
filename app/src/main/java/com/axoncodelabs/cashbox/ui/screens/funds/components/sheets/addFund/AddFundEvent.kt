package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addFund

sealed class AddFundEvent {
    data class OnNameChange(val name: String) : AddFundEvent()
    data class OnAmountChange(val amount: String) : AddFundEvent()
    data class OnDescriptionChange(val description: String) : AddFundEvent()
    object OnSaveClick : AddFundEvent()
}