package com.axoncodelabs.cashbox.ui.components.sheets.editTransaction

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class EditTransactionEvent {
    data class OnFundChanged(val fund: FundEntity) : EditTransactionEvent()
    data class OnAmountChange(val amount: String) : EditTransactionEvent()
    data class OnDescriptionChange(val description: String) : EditTransactionEvent()
    data class OnDateChange(val newDate: Long) : EditTransactionEvent()
    object OnSaveClick : EditTransactionEvent()
    object OnCancelClick : EditTransactionEvent()

    // Delete Transaction Popup events
    object OnDeleteClick : EditTransactionEvent()
}