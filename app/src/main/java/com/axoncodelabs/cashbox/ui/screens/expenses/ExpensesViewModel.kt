package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.lifecycle.ViewModel
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
//    val expenses = repository.getExpensesByDate()
}