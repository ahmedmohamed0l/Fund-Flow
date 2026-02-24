package com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.editExpense

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class EditExpenseEvent {
    data class OnFundChanged(val fund: FundEntity) : EditExpenseEvent()
    data class OnAmountChange(val amount: String) : EditExpenseEvent()
    data class OnDescriptionChange(val description: String) : EditExpenseEvent()
    object OnSaveClick : EditExpenseEvent()
    object OnCancelClick : EditExpenseEvent()

    //Delete Expense Popup events
    object OnDeleteClick : EditExpenseEvent()
}