package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.fundoptions

sealed class FundOptionsEvent {
    object OnEditFundClick : FundOptionsEvent()
    data class OnNameChange(val name: String) : FundOptionsEvent()
    object OnSaveClick : FundOptionsEvent()
    object OnExceptFundToggle : FundOptionsEvent()


    //  data class OnExceptBalance(val fund: FundEntity) : FundOptionsEvent()
    //Delete Fund Transactions Popup events
    object OnDeleteClick : FundOptionsEvent()
    object OnCancelClick : FundOptionsEvent()
}