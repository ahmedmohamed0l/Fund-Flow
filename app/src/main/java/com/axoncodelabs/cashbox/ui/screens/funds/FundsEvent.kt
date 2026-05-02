package com.axoncodelabs.cashbox.ui.screens.funds

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

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