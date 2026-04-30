package com.axoncodelabs.cashbox.ui.screens.expenses

sealed class ExpensesEvent {
    // Sheet
    data class SheetDisplayed(val sheet: ExpensesSheets) : ExpensesEvent()
    object CloseSheet : ExpensesEvent()

    // Popup
    data class PopupDisplayed(val popup: ExpensesPopup) : ExpensesEvent()
    object ClosePopup : ExpensesEvent()

    // DatePickerEvents
    object OnPreviousDayClick : ExpensesEvent()
    object OnNextDayClick : ExpensesEvent()
    data class OnDateSelected(val date: Long) : ExpensesEvent()
}