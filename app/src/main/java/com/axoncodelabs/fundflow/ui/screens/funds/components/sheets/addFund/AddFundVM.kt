package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.addFund

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionType
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.ui.components.sheets.SheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFundVM @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {

    //──── UI State ────
    var name by mutableStateOf("")
        private set
    var amount by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isNameEmpty by mutableStateOf(false)
        private set

    //──── Helpers ────
    fun clearSheetData() {
        name = ""
        amount = ""
        description = ""
        isNameEmpty = false
    }

    //──── Init ────
    fun initData() {
        //Clear old
        clearSheetData()
    }

    //──── Events ────
    private val _endSheetEvent = Channel<SheetResult>(Channel.CONFLATED)
    val endSheetEvent = _endSheetEvent.receiveAsFlow()

    fun onEvent(event: AddFundEvent) {
        when (event) {
            is AddFundEvent.OnNameChange -> {
                name = event.name
                isNameEmpty = false
            }

            is AddFundEvent.OnAmountChange -> {
                amount = event.amount
            }

            is AddFundEvent.OnDescriptionChange -> {
                description = event.description
            }

            AddFundEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val initialAmount = amount.toDoubleOrNull() ?: 0.0
                    if (name.isBlank()) {
                        isNameEmpty = true
                        return@launch
                    }

                    val newFund = FundEntity(
                        name = name,
                        balance = 0.0
                    )

                    val fundId = repository.insertFund(newFund).toInt()
                    repository.insertTransaction(
                        TransactionEntity(
                            fundId = fundId,
                            amount = initialAmount,
                            description = description.ifBlank { "القيمة الأولية للصندوق" },
                            type = TransactionType.INCOME
                        )
                    )
                    clearSheetData()
                    _endSheetEvent.send(SheetResult.Added)
                }
            }
        }
    }
}