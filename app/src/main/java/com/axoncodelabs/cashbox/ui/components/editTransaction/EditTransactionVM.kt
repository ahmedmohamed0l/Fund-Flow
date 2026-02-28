package com.axoncodelabs.cashbox.ui.components.editTransaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.expenses.ExpensesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    var transaction by mutableStateOf<TransactionWithFund?>(null)
        private set

    var fund by mutableStateOf<FundEntity?>(null)
        private set
    var fundName by mutableStateOf("")
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

    var showDeleteTransactionPopup by mutableStateOf(false)

    private data class EditableTransaction(
        val fund: FundEntity?,
        val amount: String,
        val description: String,
        val date: Long
    )

    private var originalEditableTransaction: EditableTransaction? = null
    private fun currentEditableTransaction(): EditableTransaction {
        return EditableTransaction(
            fund = fund,
            amount = amount,
            description = description,
            date = selectedDate
        )
    }

    fun initTransaction(transaction: TransactionWithFund) {
        this.transaction = transaction
        fund = transaction.fund
        fundName = transaction.fund.name
        amount = transaction.transaction.amount.toString()
        description = transaction.transaction.description
        selectedDate = transaction.transaction.date

        originalEditableTransaction = currentEditableTransaction()
        updateSaveBttnState()
    }

    private val _expensesEvent = Channel<ExpensesEvent>()
    val expensesEvent = _expensesEvent.receiveAsFlow()

    fun onEvent(event: EditTransactionEvent) {
        when (event) {
            is EditTransactionEvent.OnFundChanged -> {
                fund = event.fund
                fundName = event.fund.name
                updateSaveBttnState()
            }

            is EditTransactionEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
                updateSaveBttnState()
            }

            is EditTransactionEvent.OnDescriptionChange -> {
                description = event.description
                isDescriptionEmpty = false
                updateSaveBttnState()
            }

            is EditTransactionEvent.OnDateChange -> {
                selectedDate = event.newDate
                updateSaveBttnState()
            }

            EditTransactionEvent.OnSaveClick -> {
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
                    transaction?.transaction?.let {
                        repository.updateTransaction(
                            it.copy(
                                fundId = fund!!.id,
                                amount = amountDouble,
                                description = description,
                                date = selectedDate
                            )
                        )
                    }
                    sendExpensesEvent(ExpensesEvent.CloseSheet)
                }
            }

            EditTransactionEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    transaction?.transaction?.let { transaction ->
                        repository.deleteTransaction(transaction)
                    }
                }
                sendExpensesEvent(ExpensesEvent.ClosePopup)
                sendExpensesEvent(ExpensesEvent.CloseSheet)
            }

            EditTransactionEvent.OnCancelClick -> {
                sendExpensesEvent(ExpensesEvent.CloseSheet)
            }
        }
    }

    fun updateSaveBttnState() {
        isSaveBttnEnabled = originalEditableTransaction != currentEditableTransaction()
    }

    private fun sendExpensesEvent(event: ExpensesEvent) {
        viewModelScope.launch {
            _expensesEvent.send(event)
        }
    }
}