package com.axoncodelabs.cashbox.ui.screens.funds

sealed class FundsEvent {
    data class SheetDisplayed(val sheet: FundsSheets) : FundsEvent()
    object CloseSheet : FundsEvent()
    data class PopupDisplay(val popup: FundsPopup) : FundsEvent()
    object ClosePopup : FundsEvent()
}