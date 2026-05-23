package com.axoncodelabs.fundflow.ui.components.sheets.editTransaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class EditTransactionVM @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {

    //──── UI State ────
    var transaction by mutableStateOf<TransactionWithFund?>(null)
        private set

    var fund by mutableStateOf<FundEntity?>(null)
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

    //──── Internal Models ────
    private data class EditableTransaction(
        val fund: FundEntity?,
        val amount: String,
        val description: String,
        val date: Long
    )

    private var originalEditableTransaction: EditableTransaction? = null

    //──── Data Flows ────
    private fun currentEditableTransaction(): EditableTransaction {
        return EditableTransaction(
            fund = fund,
            amount = amount,
            description = description,
            date = selectedDate
        )
    }

    //──── Helpers ────
    fun clearSheetErrsStates() {
        isAmountEmpty = false
        isDescriptionEmpty = false
    }

    fun updateSaveBttnState() {
        isSaveBttnEnabled = originalEditableTransaction != currentEditableTransaction()
    }

    //──── Init ────
    fun initTransaction(transaction: TransactionWithFund) {
        this.transaction = transaction
        fund = transaction.fund
        amount = BigDecimal
            .valueOf(transaction.transaction.amount)
            .stripTrailingZeros()
            .toPlainString()
        description = transaction.transaction.description
        selectedDate = transaction.transaction.date

        clearSheetErrsStates()
        originalEditableTransaction = currentEditableTransaction()
        updateSaveBttnState()
    }

    //──── Events ────
    private val _closeEvent = Channel<Unit>(Channel.CONFLATED)
    val closeEvent = _closeEvent.receiveAsFlow()

    fun onEvent(event: EditTransactionEvent) {
        when (event) {
            is EditTransactionEvent.OnFundChanged -> {
                fund = event.fund
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

                    val selectedFund = fund ?: return@launch
                    transaction?.transaction?.let {
                        repository.updateTransaction(
                            it.copy(
                                fundId = selectedFund.id,
                                amount = amountDouble,
                                description = description,
                                date = selectedDate
                            )
                        )
                    }
                    _closeEvent.send(Unit)
                }
            }

            EditTransactionEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    transaction?.transaction?.let { transaction ->
                        repository.deleteTransaction(transaction)
                    }
                    _closeEvent.send(Unit)
                }
            }

            EditTransactionEvent.OnCancelClick -> {
                viewModelScope.launch {
                    _closeEvent.send(Unit)
                }
            }
        }
    }
}