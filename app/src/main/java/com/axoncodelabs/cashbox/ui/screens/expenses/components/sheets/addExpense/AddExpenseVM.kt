package com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.expenses.ExpensesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel(){
    var fund by mutableStateOf<FundEntity?>(null)
        private set
    var fundName by mutableStateOf("")
        private set
    var fundBalance by mutableDoubleStateOf(0.0)
        private set

    var isNoFundSelected by mutableStateOf(false)
        private set

    var availableBalance by mutableDoubleStateOf(0.00)
        private set

    var isAvailableNegative by mutableStateOf(false)
        private set

    var amount by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isAmountEmpty by mutableStateOf(false)
        private set
    var isDescriptionEmpty by mutableStateOf(false)
        private set
    var selectedDate by mutableLongStateOf(System.currentTimeMillis())
        private set

    private fun updateAvailableBalance() {
        val amount = amount.toDoubleOrNull() ?: 0.0
        val delta = fundBalance - amount
        availableBalance = delta
        isAvailableNegative = delta < 0
    }

    fun initTransaction(selectedDate: Long) {
        this.selectedDate = selectedDate
        clearSheetData()
    }

    private val _expensesEvent = Channel<ExpensesEvent>()
    val expensesEvent = _expensesEvent.receiveAsFlow()

    fun onEvent(event: AddExpenseEvent){
        when (event) {
            is AddExpenseEvent.OnFundSelected -> {
                fund = event.fund
                fundName = event.fund.name
                fundBalance = event.fund.balance
                isNoFundSelected = false
                updateAvailableBalance()
            }

            is AddExpenseEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
                updateAvailableBalance()
            }

            is AddExpenseEvent.OnDescriptionChange -> {
                description = event.description
                isDescriptionEmpty = false
            }

            AddExpenseEvent.OnSaveClick -> {
                viewModelScope.launch {
                    fund ?: run {
                        isNoFundSelected = true
                        return@launch
                    }

                    val amountDouble = amount.toDoubleOrNull()
                    if (amount.isBlank() || amountDouble == null) {
                        isAmountEmpty = true
                        return@launch
                    }

                    if (description.isBlank()) {
                        isDescriptionEmpty = true
                        return@launch
                    }

                    repository.insertTransaction(
                        TransactionEntity(
                            fundId = fund!!.id,
                            amount = amountDouble,
                            description = description,
                            date = selectedDate,
                            type = TransactionType.EXPENSE,
                            isTransfer = false
                        )
                    )
                    clearSheetData()
                    sendExpensesEvent(ExpensesEvent.CloseSheet)
                }
            }
        }
    }

    fun clearSheetData() {
        fund = null
        fundName = ""
        fundBalance = 0.0
        isNoFundSelected = false
        amount = ""
        description = ""
        isAmountEmpty = false
        isDescriptionEmpty = false
        updateAvailableBalance()
    }

    private fun sendExpensesEvent(event: ExpensesEvent) {
        viewModelScope.launch {
            _expensesEvent.send(event)
        }
    }
}