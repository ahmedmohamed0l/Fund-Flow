package com.axoncodelabs.fundflow.ui.screens.expenses.components.sheets.addExpense

import com.axoncodelabs.fundflow.data.local.entity.FundEntity

sealed class AddExpenseEvent {
    data class OnFundSelected(val fund: FundEntity) : AddExpenseEvent()
    data class OnAmountChange(val amount: String) : AddExpenseEvent()
    data class OnDescriptionChange(val description: String) : AddExpenseEvent()
    object OnSaveClick : AddExpenseEvent()
}