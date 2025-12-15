package com.axoncodelabs.cashbox.ui.screens.expenses

import com.axoncodelabs.cashbox.data.local.relation.ExpenseWithFund

sealed class ExpensesEvent {
    // Sheet
    data class SheetDisplayed(val sheet: ExpensesSheets) : ExpensesEvent()
    object CloseSheet : ExpensesEvent()

    //DatePickerEvents
    object OnPreviousDayClick : ExpensesEvent()
    object OnNextDayClick : ExpensesEvent()
    object OnToggleDatePicker : ExpensesEvent()
    data class OnDateSelected(val date: Long) : ExpensesEvent()

    data class OnExpenseClick(val expense: ExpenseWithFund) : ExpensesEvent()
    object OnAddExpense : ExpensesEvent()
}