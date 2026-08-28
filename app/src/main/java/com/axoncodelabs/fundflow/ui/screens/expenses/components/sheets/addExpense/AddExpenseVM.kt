package com.axoncodelabs.fundflow.ui.screens.expenses.components.sheets.addExpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionType
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.ui.components.sheets.SheetResult
import com.axoncodelabs.fundflow.ui.util.getAdjustedTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseVM @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {

    //──── UI State ────
    var fund by mutableStateOf<FundEntity?>(null)
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

    //──── Helpers ────
    fun clearSheetData() {
        fund = null
        isNoFundSelected = false
        amount = ""
        description = ""
        isAmountEmpty = false
        isDescriptionEmpty = false
        updateAvailableBalance()
    }

    private fun updateAvailableBalance() {
        val amount = amount.toDoubleOrNull() ?: 0.0
        val delta = (fund?.balance ?: 0.0) - amount
        availableBalance = delta
        isAvailableNegative = delta < 0
    }

    //──── Init ────
    fun initTransaction(selectedDate: Long) {
        this.selectedDate = selectedDate
        clearSheetData()
    }

    //──── Events ────
    private val _endSheetEvent = Channel<SheetResult>(Channel.CONFLATED)
    val endSheetEvent = _endSheetEvent.receiveAsFlow()

    fun onEvent(event: AddExpenseEvent) {
        when (event) {
            is AddExpenseEvent.OnFundSelected -> {
                fund = event.fund
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
                    if (amount.isBlank() || amountDouble == null || amountDouble == 0.0) {
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
                            date = getAdjustedTime(selectedDate),
                            type = TransactionType.EXPENSE,
                            isTransfer = false
                        )
                    )
                    clearSheetData()
                    _endSheetEvent.send(SheetResult.Added)
                }
            }
        }
    }
}