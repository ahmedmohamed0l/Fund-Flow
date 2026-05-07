package com.axoncodelabs.fundflow.ui.screens.expenses

import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund

sealed class ExpensesSheets {
    object None : ExpensesSheets()
    object AddExpense : ExpensesSheets()
    data class EditExpense(val expense: TransactionWithFund) : ExpensesSheets()
}

sealed class ExpensesPopup {
    object None : ExpensesPopup()
    data class DatePicker(val initialDate: Long) : ExpensesPopup()
}

data class ExpensesState(
    val currentSheet: ExpensesSheets = ExpensesSheets.None,
    val currentPopup: ExpensesPopup = ExpensesPopup.None,

    val isHideData: Boolean = false,

    val selectedDate: Long = System.currentTimeMillis(),

    val expenses: List<TransactionWithFund> = emptyList(),
    val expensesTotalValue: Double = 0.0,
)