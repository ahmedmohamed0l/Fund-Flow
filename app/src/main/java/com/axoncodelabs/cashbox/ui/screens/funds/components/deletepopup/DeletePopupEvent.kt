package com.axoncodelabs.cashbox.ui.screens.funds.components.deletepopup

sealed class DeletePopupEvent {
    object OnDeleteClick : DeletePopupEvent()
    object OnCancelClick : DeletePopupEvent()
}