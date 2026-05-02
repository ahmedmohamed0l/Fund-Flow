package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao.DateRange
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.util.endOfMonth
import com.axoncodelabs.cashbox.ui.util.startOfMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    //── State ──
    private val _state = MutableStateFlow(ReportsState())
    val state = _state.asStateFlow()

    //──── Helpers ────
    private val _clearExpandedDaysEvent = MutableSharedFlow<Unit>()
    val clearExpandedDaysEvent: SharedFlow<Unit> = _clearExpandedDaysEvent

    fun clearExpandedDays() {
        viewModelScope.launch {
            _clearExpandedDaysEvent.emit(Unit)
        }
    }

    // ────────( Data Flows )────────
    private val selectedFundFlow = _state
        .map { it.selectedFund }
        .distinctUntilChanged()

    private val selectedDateFlow = _state
        .map { it.selectedDate }
        .distinctUntilChanged()

    private val initialDateRange: Flow<DateRange> = repository.getFirstAndLastDate()
        .distinctUntilChanged()

    private val exceptTransfersFlow = _state
        .map { it.exceptTransfers }
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

    }.onEach { clearExpandedDays() }
        .distinctUntilChanged()

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

            else -> repository.getTransactionsByFundAndDateAndType(
                fundId = filter.fund.id,
                type = type,
                startDate = filter.startDate,
                endDate = filter.endDate
            )
        }
    }

    //──── Expenses Flow ────
    private val expensesListFlow = queryFilterFlow
        .flatMapLatest { getTransactionListFlow(it, TransactionType.EXPENSE) }

    private val expensesWithSumFlow = combine(
        expensesListFlow,
        exceptTransfersFlow
    ) { list, exceptTransfers ->
        val sum = list
            .filter { !exceptTransfers || !it.transaction.isTransfer }
            .sumOf { it.transaction.amount }
        list to sum
    }

    //──── Income Flow ────
    private val incomeListFlow = queryFilterFlow
        .flatMapLatest { getTransactionListFlow(it, TransactionType.INCOME) }

    private val incomeWithSumFlow = combine(
        incomeListFlow,
        exceptTransfersFlow
    ) { list, exceptTransfers ->
        val sum = list
            .filter { !exceptTransfers || !it.transaction.isTransfer }
            .sumOf { it.transaction.amount }
        list to sum
    }

    //──── Init ────
    init {
        combine(
            repository.hideDataFlow,
            expensesWithSumFlow,
            incomeWithSumFlow,
        ) { isHideData, (expenses, expensesSum), (income, incomeSum) ->

            _state.value.copy(
                isHideData = isHideData,
                expensesList = expenses,
                expensesSum = expensesSum,
                incomeList = income,
                incomeSum = incomeSum
            )
        }.onEach { newState ->
            _state.update { newState }
        }.launchIn(viewModelScope)
    }

    //──── Events ────
    fun onEvent(event: ReportsEvent) {
        when (event) {
            //── Sheets ──
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

            //── Fund Selection Events ──
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

            //── Date Selection Events ──
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

            //── Report Events ──
            ReportsEvent.OnToggleTransfers -> {
                _state.update {
                    it.copy(
                        exceptTransfers = !it.exceptTransfers
                    )
                }
            }

            is ReportsEvent.OnReportTypeChange -> {
                _state.update {
                    it.copy(
                        selectedReportType = event.reportType
                    )
                }
                clearExpandedDays()
            }
        }
    }
}