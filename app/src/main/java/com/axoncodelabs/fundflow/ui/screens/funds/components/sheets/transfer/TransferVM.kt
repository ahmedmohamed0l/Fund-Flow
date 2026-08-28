package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.transfer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.data.util.StringProvider
import com.axoncodelabs.fundflow.ui.components.sheets.SheetResult
import com.axoncodelabs.fundflow.ui.util.getAdjustedTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferVM @Inject constructor(
    private val repository: FundFlowRepository,
    private val stringProvider: StringProvider,
) : ViewModel() {

    //──── UI State ────
    var fromFund by mutableStateOf<FundEntity?>(null)
        private set

    var toFund by mutableStateOf<FundEntity?>(null)
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
    var selectedDate by mutableLongStateOf(System.currentTimeMillis())
        private set

    //──── Helpers ────
    private fun updateAvailableBalance() {
        val amount = amount.toDoubleOrNull() ?: 0.0
        val delta = (fromFund?.balance ?: 0.0) - amount
        availableBalance = delta
        isAvailableNegative = delta < 0
    }

    fun clearSheetData() {
        toFund = null
        isNoFundSelected = false
        amount = ""
        description = ""
        isAmountEmpty = false
        selectedDate = System.currentTimeMillis()
        updateAvailableBalance()
    }

    //──── Init ────
    fun initTransaction(fundFrom: FundEntity) {
        this.fromFund = fundFrom
        clearSheetData()
    }

    //──── Events ────
    private val _endSheetEvent = Channel<SheetResult>(Channel.CONFLATED)
    val endSheetEvent = _endSheetEvent.receiveAsFlow()

    fun onEvent(event: TransferEvent) {
        when (event) {
            is TransferEvent.OnToFundSelected -> {
                toFund = event.fund
                isNoFundSelected = false
            }

            is TransferEvent.OnAmountChange -> {
                amount = event.amount
                isAmountEmpty = false
                updateAvailableBalance()
            }

            is TransferEvent.OnDescriptionChange -> {
                description = event.description
            }

            is TransferEvent.OnDateChange -> {
                selectedDate = event.newDate
            }

            TransferEvent.OnSaveClick -> {
                viewModelScope.launch {
                    val source = fromFund!!
                    val target = toFund ?: run {
                        isNoFundSelected = true
                        return@launch
                    }

                    val amountDouble = amount.toDoubleOrNull()
                    if (amount.isBlank() || amountDouble == null || amountDouble == 0.0) {
                        isAmountEmpty = true
                        return@launch
                    }

                    if (description.isBlank()) {
                        description =
                            (stringProvider.getString(R.string.Sheet_TransferFromDescription)) + " " + "(${source.name})" + " " +
                                    (stringProvider.getString(R.string.Sheet_TransferToDescription)) + " " + "(${target.name})"
                    }

                    repository.transferBetweenFunds(
                        fromFundId = source.id,
                        toFundId = target.id,
                        amount = amountDouble,
                        description = description,
                        timestamp = getAdjustedTime(selectedDate)
                    )

                    clearSheetData()
                    _endSheetEvent.send(SheetResult.Added)
                }
            }
        }
    }
}