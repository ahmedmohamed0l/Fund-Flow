package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao.DateRange
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    private val selectedFundFlow = _state
        .map { it.selectedFund }
        .distinctUntilChanged()

    private val selectedDateFlow = _state
        .map { it.selectedDate }
        .distinctUntilChanged()

    private val initialDateRange: Flow<DateRange> = repository.getFirstAndLastDate()
        .distinctUntilChanged()

    private val includeTransfersFlow = _state
        .map { it.includeTransfers }
        .distinctUntilChanged()

    private val queryFilterFlow = combine(
        selectedFundFlow,
        selectedDateFlow,
        initialDateRange
    ) { fund, selectedDate, dbRange ->

        val startDate: Long?
        val endDate: Long?

        when {
            selectedDate != null -> {
                startDate = selectedDate.startOfMonth()
                endDate = selectedDate.endOfMonth()
            }

            dbRange.firstDate != null && dbRange.lastDate != null -> {
                startDate = dbRange.firstDate.startOfMonth()
                endDate = dbRange.lastDate.endOfMonth()
            }

            else -> {
                startDate = null
                endDate = null
            }
        }

        QueryFilter(
            fund = fund,
            startDate = startDate,
            endDate = endDate
        )

    }.distinctUntilChanged()

    private fun getTransactionListFlow(
        filter: QueryFilter,
        type: TransactionType
    ): Flow<List<TransactionWithFund>> {
        return when {
            filter.startDate == null || filter.endDate == null -> flowOf(emptyList())

            filter.fund == null -> repository.getTransactionsByDateAndType(
                type = type,
                startDate = filter.startDate,
                endDate = filter.endDate
            )

            else -> repository.getTransactionsByFundAndTypeAndDate(
                fundId = filter.fund.id,
                type = type,
                startDate = filter.startDate,
                endDate = filter.endDate
            )
        }
    }

    /**.....( Expenses Flow ).....**/
    private val expensesListFlow = queryFilterFlow
        .flatMapLatest { getTransactionListFlow(it, TransactionType.EXPENSE) }

    private val expensesWithSumFlow = combine(
        expensesListFlow,
        includeTransfersFlow
    ) { list, includeTransfers ->
        val sum = list
            .filter { includeTransfers || !it.transaction.isTransfer }
            .sumOf { it.transaction.amount }
        list to sum
    }

    /**.....( Income Flow ).....**/
    private val incomeListFlow = queryFilterFlow
        .flatMapLatest { getTransactionListFlow(it, TransactionType.INCOME) }

    private val incomeWithSumFlow = combine(
        incomeListFlow,
        includeTransfersFlow
    ) { list, includeTransfers ->
        val sum = list
            .filter { includeTransfers || !it.transaction.isTransfer }
            .sumOf { it.transaction.amount }
        list to sum
    }

    init {
        combine(
            repository.hideDataFlow,
            expensesWithSumFlow,
            incomeWithSumFlow,
        ) { isHideData,
            (expenses, expensesSum),
            (income, incomeSum) ->

            Triple(isHideData, expenses to expensesSum, income to incomeSum)
        }.onEach { (isHideData, expensesPair, incomePair) ->
            _state.update { currentState ->
                currentState.copy(
                    isHideData = isHideData,
                    expensesList = expensesPair.first,
                    expensesSum = expensesPair.second,
                    incomeList = incomePair.first,
                    incomeSum = incomePair.second
                )
            }
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

            is ReportsEvent.OnFundChanged -> {
                _state.update {
                    it.copy(
                        selectedFund = event.fund,
                        isSelectAllFunds = false,
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            ReportsEvent.OnSelectAllFunds -> {
                _state.update {
                    it.copy(
                        selectedFund = null,
                        isSelectAllFunds = true,
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            is ReportsEvent.OnDateChange -> {
                _state.update {
                    it.copy(
                        selectedDate = event.date,
                        isSelectAllDates = false,
                        isSelectCurrentMonth = false,
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            ReportsEvent.OnSelectAllDates -> {
                _state.update {
                    it.copy(
                        selectedDate = null,
                        isSelectAllDates = true,
                        isSelectCurrentMonth = false,
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            ReportsEvent.OnSelectCurrentMonth -> {
                _state.update {
                    it.copy(
                        selectedDate = System.currentTimeMillis(),
                        isSelectAllDates = false,
                        isSelectCurrentMonth = true,
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            ReportsEvent.OnToggleTransfers -> {
                _state.update {
                    it.copy(
                        includeTransfers = !it.includeTransfers
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