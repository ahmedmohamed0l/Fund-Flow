package com.axoncodelabs.fundflow.ui.screens.funds

import com.axoncodelabs.fundflow.data.local.entity.FundEntity

sealed class FundsEvent {
    // Sheet
    data class SheetDisplayed(val sheet: FundsSheets) : FundsEvent()
    object CloseSheet : FundsEvent()

    // Popup
    data class PopupDisplay(val popup: FundsPopup) : FundsEvent()
    object ClosePopup : FundsEvent()

    // Delete Fund popup
    data class DeleteFund(val fund: FundEntity) : FundsEvent()
}