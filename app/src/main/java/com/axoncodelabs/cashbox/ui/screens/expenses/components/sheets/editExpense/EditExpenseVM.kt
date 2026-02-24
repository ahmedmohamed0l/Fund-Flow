package com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.editExpense

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
import com.axoncodelabs.cashbox.data.local.relation.ExpenseWithFund
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExpenseVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    var expense by mutableStateOf<ExpenseWithFund?>(null)
        private set

    var fund by mutableStateOf<FundEntity?>(null)
        private set
    var fundName by mutableStateOf("")
        private set
    var fundBalance by mutableDoubleStateOf(0.0)
        private set

    var availableBalance by mutableDoubleStateOf(0.0)
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

    var isSaveBttnEnabled by mutableStateOf(false)
        private set

    var showDeleteExpensePopup by mutableStateOf(false)

    private data class EditableExpense(
        val fund: FundEntity?,
        val amount: String,
        val description: String,
        val date: Long
    )

    private var originalEditableExpense: EditableExpense? = null
    private fun currentEditableExpense(): EditableExpense {
        return EditableExpense(
            fund = fund,
            amount = amount,
            description = description,
            date = selectedDate
        )
    }

    fun initTransaction(expense: ExpenseWithFund) {
        this.expense = expense
        fund = expense.fund
        fundName = expense.fund.name
        amount = expense.transaction.amount.toString()
        description = expense.transaction.description
        selectedDate = expense.transaction.date

        fundBalance = expense.fund.balance
        updateAvailableBalance()

        originalEditableExpense = currentEditableExpense()
        updateSaveBttnState()
    }

    private fun updateAvailableBalance() {
        val amount = amount.toDoubleOrNull() ?: 0.0
        val delta = if (fund?.id == originalEditableExpense?.fund?.id) {
            val originalAmount = originalEditableExpense?.amount?.toDoubleOrNull() ?: 0.0
            fundBalance + originalAmount - amount
        } else {
            fundBalance - amount
        }
        availableBalance = delta
        isAvailableNegative = delta < 0
    }

    private val _fundsEvent = Channel<FundsEvent>()
    val fundsEvent = _fundsEvent.receiveAsFlow()

    fun onEvent(event: EditExpenseEvent) {
        when (event) {
            is EditExpenseEvent.OnFundChanged -> {
                fund = event.fund
                fundName = event.fund.name
                updateAvailableBalance()
                updateSaveBttnState()
            }

            is EditExpenseEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
                updateAvailableBalance()
                updateSaveBttnState()
            }

            is EditExpenseEvent.OnDescriptionChange -> {
                description = event.description
                isDescriptionEmpty = false
                updateSaveBttnState()
            }

            EditExpenseEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val amountDouble = amount.toDoubleOrNull()
                    if (amount.isBlank() || amountDouble == null) {
                        isAmountEmpty = true
                        return@launch
                    }

                    if (description.isBlank()) {
                        isDescriptionEmpty = true
                        return@launch
                    }
                    repository.updateTransaction(
                        TransactionEntity(
                            fundId = fund!!.id,
                            amount = amountDouble,
                            description = description,
                            date = selectedDate,
                            type = TransactionType.EXPENSE,
                            isTransfer = false
                        )
                    )
                    sendFundsEvent(FundsEvent.CloseSheet)
                }
            }

            EditExpenseEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    expense?.transaction?.let { transaction ->
                        repository.deleteTransaction(transaction)
                    }
                }
                sendFundsEvent(FundsEvent.ClosePopup)
                sendFundsEvent(FundsEvent.CloseSheet)
            }

            EditExpenseEvent.OnCancelClick -> {
                sendFundsEvent(FundsEvent.CloseSheet)
            }
        }
    }

    fun updateSaveBttnState() {
        isSaveBttnEnabled = originalEditableExpense != currentEditableExpense()
    }

    private fun sendFundsEvent(event: FundsEvent) {
        viewModelScope.launch {
            _fundsEvent.send(event)
        }
    }
}