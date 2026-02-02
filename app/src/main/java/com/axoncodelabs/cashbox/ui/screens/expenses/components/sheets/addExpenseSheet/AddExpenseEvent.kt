package com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpenseSheet

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class AddExpenseEvent {
    data class OnFundSelected(val fund: FundEntity) : AddExpenseEvent()
    data class OnAmountChange(val amount: String) : AddExpenseEvent()
    data class OnDescriptionChange(val description: String) : AddExpenseEvent()
    object OnSaveClick : AddExpenseEvent()
}