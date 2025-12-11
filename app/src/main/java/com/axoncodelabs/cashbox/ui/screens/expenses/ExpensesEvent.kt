package com.axoncodelabs.cashbox.ui.screens.expenses

sealed class ExpensesEvent {
    data class SheetDisplayed(val sheet: ExpensesSheets) : ExpensesEvent()
    object CloseSheet : ExpensesEvent()

    //DatePickerEvents
    object OnPreviousDayClick : ExpensesEvent()
    object OnNextDayClick : ExpensesEvent()
    object OnToggleDatePicker : ExpensesEvent()
    data class OnDateSelected(val date: Long) : ExpensesEvent()
}