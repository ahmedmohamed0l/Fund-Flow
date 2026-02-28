package com.axoncodelabs.cashbox.ui.screens.expenses

import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund

sealed class ExpensesEvent {
    // Sheet
    data class SheetDisplayed(val sheet: ExpensesSheets) : ExpensesEvent()
    object CloseSheet : ExpensesEvent()

    // Popup
    data class PopupDisplay(val popup: ExpensesPopup) : ExpensesEvent()
    object ClosePopup : ExpensesEvent()

    //DatePickerEvents
    object OnPreviousDayClick : ExpensesEvent()
    object OnNextDayClick : ExpensesEvent()
    object OnToggleDatePicker : ExpensesEvent()
    data class OnDateSelected(val date: Long) : ExpensesEvent()

    data class OnExpenseClick(val expense: TransactionWithFund) : ExpensesEvent()
    object OnAddExpense : ExpensesEvent()
}