package com.axoncodelabs.cashbox.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
    private val _selectedDate = mutableStateOf(System.currentTimeMillis())
    private val _transactions = mutableStateOf<List<TransactionEntity>>(emptyList())

    val transactions: State<List<TransactionEntity>> = _transactions
    val selectedDate: State<Long> = _selectedDate

    // States للـ Dialogs
    private val _showAddExpenseDialog = mutableStateOf(false)
    val showAddExpenseDialog: State<Boolean> = _showAddExpenseDialog

    private val _showDatePickerDialog = mutableStateOf(false)
    val showDatePickerDialog: State<Boolean> = _showDatePickerDialog
}