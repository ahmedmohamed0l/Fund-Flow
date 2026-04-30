package com.axoncodelabs.cashbox.ui.screens.funds

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class FundsEvent {
    data class SheetDisplayed(val sheet: FundsSheets) : FundsEvent()
    object CloseSheet : FundsEvent()
    data class PopupDisplay(val popup: FundsPopup) : FundsEvent()
    object ClosePopup : FundsEvent()

    data class DeleteFund(val fund: FundEntity) : FundsEvent()
}