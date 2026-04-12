package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ReportsState())
    val state = _state.asStateFlow()

    private val selectedFundFlow = state
        .map { it.selectedFund }
        .distinctUntilChanged()

    private val selectedDateFlow = state
        .map { it.selectedDate }
        .distinctUntilChanged()

    private val selectedFundAndDateFlow = combine(
        selectedFundFlow,
        selectedDateFlow
    ) { fund, date ->
        fund to date
    }.distinctUntilChanged()

    /**.....( Expenses Flow ).....**/
    private val expensesListFlow = selectedFundAndDateFlow
        .flatMapLatest { (fund, date) ->
            if (fund == null) {
                repository.getTransactionsByDateAndType(
                    type = TransactionType.EXPENSE,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            } else {
                repository.getTransactionsByFundAndTypeAndDate(
                    fundId = fund.id,
                    type = TransactionType.EXPENSE,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            }
        }

    private val expensesSumFlow = selectedFundAndDateFlow
        .flatMapLatest { (fund, date) ->
            if (fund == null) {
                repository.getTransactionsSumByDateAndType(
                    type = TransactionType.EXPENSE,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            } else {
                repository.getTransactionsSumByFundAndTypeAndDate(
                    fundId = fund.id,
                    type = TransactionType.EXPENSE,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            }
        }

    private val expensesWithSumFlow = combine(
        expensesListFlow,
        expensesSumFlow
    ) { expensesList, sum ->
        expensesList to sum
    }

    /**.....( Income Flow ).....**/
    private val incomeListFlow = selectedFundAndDateFlow
        .flatMapLatest { (fund, date) ->
            if (fund == null) {
                repository.getTransactionsByDateAndType(
                    type = TransactionType.INCOME,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            } else {
                repository.getTransactionsByFundAndTypeAndDate(
                    fundId = fund.id,
                    type = TransactionType.INCOME,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            }
        }

    private val incomeSumFlow = selectedFundAndDateFlow
        .flatMapLatest { (fund, date) ->
            if (fund == null) {
                repository.getTransactionsSumByDateAndType(
                    type = TransactionType.INCOME,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            } else {
                repository.getTransactionsSumByFundAndTypeAndDate(
                    fundId = fund.id,
                    type = TransactionType.INCOME,
                    startDate = date!!.startOfMonth(),
                    endDate = date.endOfMonth()
                )
            }
        }

    private val incomeWithSumFlow = combine(
        incomeListFlow,
        incomeSumFlow
    ) { incomeList, sum ->
        incomeList to sum
    }

    init {
        combine(
            selectedFundAndDateFlow,
            repository.hideDataFlow,
            expensesWithSumFlow,
            incomeWithSumFlow,
        ) { (selectedFund, selectedDate),
            isHideData,
            (expenses, expensesSum),
            (income, incomeSum) ->

            _state.value.copy(
                selectedFund = selectedFund,
                selectedDate = selectedDate,
                isHideData = isHideData,
                expensesList = expenses,
                expensesSum = expensesSum,
                incomeList = income,
                incomeSum = incomeSum
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ReportsEvent) {
        when (event) {
            is ReportsEvent.SheetDisplayed -> {
                _state.update {
                    it.copy(currentSheet = event.sheet)
                }
            }
            ReportsEvent.CloseSheet -> {
                _state.update {
                    it.copy(currentSheet = ReportsSheets.None)
                }
            }

            is ReportsEvent.PopupDisplayed -> {
                _state.update {
                    it.copy(popupState = event.popup)
                }
            }
            ReportsEvent.ClosePopup -> {
                _state.update {
                    it.copy(popupState = ReportsPopups.None)
                }
            }

            is ReportsEvent.OnFundChanged -> {}
            ReportsEvent.OnSelectAllFunds -> {
                _state.update {
                    it.copy(
                        selectedFund = null,
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            is ReportsEvent.OnDateChange -> {
                _state.update {
                    it.copy(
                        selectedDate = event.date,
                        popupState = ReportsPopups.None
                    )
                }
            }

            ReportsEvent.OnSelectAllDates -> {
                _state.update {
                    it.copy(
                        selectedDate = null,
                        popupState = ReportsPopups.None
                    )
                }
            }

            is ReportsEvent.OnReportTypeChange -> {
                _state.update {
                    it.copy(
                        selectedReportType = event.reportType
                    )
                }
            }

//            is ReportsEvent.OnTransactionClick -> {}
        }
    }

    /**.....( Date Helper ).....**/
    fun Long.startOfMonth(): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = this@startOfMonth }
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }

    fun Long.endOfMonth(): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = this@endOfMonth }
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}