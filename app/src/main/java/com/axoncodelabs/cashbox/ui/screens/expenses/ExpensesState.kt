package com.axoncodelabs.cashbox.ui.screens.expenses

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity

sealed class ExpensesSheets {
    object None : ExpensesSheets()
    object AddExpense : ExpensesSheets()
    data class EditExpense(val fund: FundEntity) : ExpensesSheets()
}

data class ExpensesState(
    val isHideData: Boolean = false,
    val expenses: List<TransactionEntity> = emptyList(),

    //DatePickerData
    val selectedDate: Long = System.currentTimeMillis(),
    val isDatePickerOpen: Boolean = false
)