package com.axoncodelabs.cashbox.ui.components.deletepopup

sealed class DeletePopupEvent {
    object OnDeleteClick : DeletePopupEvent()
    object OnCancelClick : DeletePopupEvent()
}