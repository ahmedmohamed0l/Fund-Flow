package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.lifecycle.ViewModel
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ReportsState())
    val state = _state.asStateFlow()
/*  //Brake_1
    /**.....( Data Flow ).....**/
    private val fundsListFlow = state
        .flatMapLatest { repository.getAllFunds() }
        .filterNotNull()
        .distinctUntilChanged()

    private val datesListFlow = state
        .flatMapLatest { repository.getAllDates() }
        .filterNotNull()
        .distinctUntilChanged()

    private val fundsAndDatesListsFlow = combine(
        fundsListFlow,
        datesListFlow
    ) { fundsList, datesList ->
        fundsList to datesList
    }


    private val selectedFundFlow = state
        .map { it.selectedFund }
        .distinctUntilChanged()

    private val selectedDateFlow = state
        .map { it.selectedDate }
        .distinctUntilChanged()

    private val selectedFundAndDateFlow = combine(
        selectedFundFlow,
        selectedDateFlow
    ) { selectedFund, selectedDate ->
        selectedFund to selectedDate
    }

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
            fundsAndDatesListsFlow,
            selectedFundAndDateFlow,
            expensesWithSumFlow,
            incomeWithSumFlow,
            repository.hideDataFlow
        ) { (funds, dates),
            (selectedFund, selectedDate),
            (expenses, expensesSum),
            (income, incomeSum),
            isHideData ->

            _state.value.copy(
                fundsList = funds,
                datesList = dates,
                selectedFund = selectedFund,
                selectedDate = selectedDate,
                expensesList = expenses,
                expensesSum = expensesSum,
                incomeList = income,
                incomeSum = incomeSum,
                isHideData = isHideData
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }
*/
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
            is ReportsEvent.OnDateChange -> {}
//            is ReportsEvent.OnReportTypeChange -> {}

//            is ReportsEvent.OnTransactionClick -> {}
        }
    }

/*  //Brake_2
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
 */
}