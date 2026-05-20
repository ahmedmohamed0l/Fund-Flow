package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.fundOptions

sealed class FundOptionsEvent {
    object OnEditFundClick : FundOptionsEvent()
    data class OnSaveClick(val name: String) : FundOptionsEvent()
    data class OnExceptFundToggle(val isExcepted: Boolean) : FundOptionsEvent()


    //Delete Fund Transactions Popup events
    object OnDeleteClick : FundOptionsEvent()
    object OnCancelClick : FundOptionsEvent()
}