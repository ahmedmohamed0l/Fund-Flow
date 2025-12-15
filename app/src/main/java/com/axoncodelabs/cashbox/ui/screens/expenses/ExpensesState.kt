package com.axoncodelabs.cashbox.ui.screens.expenses

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.ExpenseWithFund

sealed class ExpensesSheets {
    object None : ExpensesSheets()
    object AddExpense : ExpensesSheets()
    data class EditExpense(val fund: FundEntity) : ExpensesSheets()
}

data class ExpensesState(
    val currentSheet: ExpensesSheets = ExpensesSheets.None,
    val expenses: List<ExpenseWithFund> = emptyList(),
    val expensesTotalValue: Double = 0.0,
    val isHideData: Boolean = false,

    //DatePickerData
    val selectedDate: Long = System.currentTimeMillis(),
    val isDatePickerOpen: Boolean = false
)