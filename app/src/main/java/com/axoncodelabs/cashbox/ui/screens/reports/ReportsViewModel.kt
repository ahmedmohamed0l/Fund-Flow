package com.axoncodelabs.cashbox.ui.screens.reports

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao.DateRange
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
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

    /*private val effectiveDateRangeFlow = combine(
        dateRangeFlow,
        selectedDateFlow
    ) { dbRange, selectedDate ->
        when {
            selectedDate != null -> {
                selectedDate.startOfMonth() to selectedDate.endOfMonth()
            }

            dbRange.firstDate != null && dbRange.lastDate != null -> {
                dbRange.firstDate to dbRange.lastDate
            }

            else -> {
                0L to Long.MAX_VALUE
            }
        }
    }.distinctUntilChanged()

    private val selectedFundAndDateFlow = combine(
        selectedFundFlow,
        effectiveDateRangeFlow
    ) { fund, date ->
        fund to date
    }.distinctUntilChanged()*/


    /**.....( Expenses Flow ).....**/
    private val expensesListFlow = queryFilterFlow
        .flatMapLatest { filter ->
            Log.d(
                "MY_TEST",
                "Selected Date = ${formatDate(filter.startDate)} to ${formatDate(filter.endDate)}"
            )
            when {
                filter.startDate == null || filter.endDate == null -> {
                    flowOf(emptyList())
                }

                filter.fund == null -> {
                    repository.getTransactionsByDateAndType(
                        type = TransactionType.EXPENSE,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }

                else -> {
                    repository.getTransactionsByFundAndTypeAndDate(
                        fundId = filter.fund.id,
                        type = TransactionType.EXPENSE,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }
            }
        }

    private val expensesSumFlow = queryFilterFlow
        .flatMapLatest { filter ->
            when {
                filter.startDate == null || filter.endDate == null -> {
                    flowOf(0.0)
                }

                filter.fund == null -> {
                    repository.getTransactionsSumByDateAndType(
                        type = TransactionType.EXPENSE,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }

                else -> {
                    repository.getTransactionsSumByFundAndTypeAndDate(
                        fundId = filter.fund.id,
                        type = TransactionType.EXPENSE,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }
            }
        }

    private val expensesWithSumFlow = combine(
        expensesListFlow,
        expensesSumFlow
    ) { expensesList, sum ->
        expensesList to sum
    }

    /**.....( Income Flow ).....**/
    private val incomeListFlow = queryFilterFlow
        .flatMapLatest { filter ->
            when {
                filter.startDate == null || filter.endDate == null -> {
                    flowOf(emptyList())
                }

                filter.fund == null -> {
                    repository.getTransactionsByDateAndType(
                        type = TransactionType.INCOME,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }

                else -> {
                    repository.getTransactionsByFundAndTypeAndDate(
                        fundId = filter.fund.id,
                        type = TransactionType.INCOME,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }
            }
        }

    private val incomeSumFlow = queryFilterFlow
        .flatMapLatest { filter ->
            when {
                filter.startDate == null || filter.endDate == null -> {
                    flowOf(0.0)
                }

                filter.fund == null -> {
                    repository.getTransactionsSumByDateAndType(
                        type = TransactionType.INCOME,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }

                else -> {
                    repository.getTransactionsSumByFundAndTypeAndDate(
                        fundId = filter.fund.id,
                        type = TransactionType.INCOME,
                        startDate = filter.startDate,
                        endDate = filter.endDate
                    )
                }
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
            queryFilterFlow,
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
                        currentSheet = ReportsSheets.None
                    )
                }
            }

            ReportsEvent.OnSelectAllDates -> {
                _state.update {
                    it.copy(
                        selectedDate = null,
                        isSelectAllDates = true,
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