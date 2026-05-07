package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.fundOptions

sealed class FundOptionsEvent {
    object OnEditFundClick : FundOptionsEvent()
    data class OnNameChange(val name: String) : FundOptionsEvent()
    object OnSaveClick : FundOptionsEvent()
    object OnExceptFundToggle : FundOptionsEvent()


    //Delete Fund Transactions Popup events
    object OnDeleteClick : FundOptionsEvent()
    object OnCancelClick : FundOptionsEvent()
}