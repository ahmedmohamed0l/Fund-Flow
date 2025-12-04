package com.axoncodelabs.cashbox.ui.screens.reports
/*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


class ReportViewModel @Inject constructor(
    private val repository: CashBoxRepository
) : ViewModel() {

    private val _state = mutableStateOf(ReportScreenState())
    val state: State<ReportScreenState> = _state

    fun onEvent(event: ReportScreenEvent) {
        when(event) {
            is ReportScreenEvent.ToggleDayCard -> {
                val current = _state.value.expandedDays.toMutableSet()
                if(current.contains(event.date)) current.remove(event.date)
                else current.add(event.date)
                _state.value = _state.value.copy(expandedDays = current)
            }
            is ReportScreenEvent.SelectExpense -> {
                _state.value = _state.value.copy(
                    selectedExpense = event.expense,
                    showEditDialog = true
                )
            }
            ReportScreenEvent.DismissDialog -> {
                _state.value = _state.value.copy(
                    selectedExpense = null,
                    showEditDialog = false
                )
            }
        }
    }

    fun loadDailyExpenses(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            repository.getExpensesByDate(startDate, endDate)
                .collect { transactions ->
                    val grouped = transactions.groupBy { it.date.toDayString() } // .toDayString() يحول Timestamp لـ "dd/MM/yyyy"
                    val dailyList = grouped.map { (date, expenses) ->
                        DailyExpense(
                            date = date,
                            total = expenses.sumOf { it.amount },
                            expenses = expenses
                        )
                    }
                    _state.value = _state.value.copy(dailyExpenses = dailyList)
                }
        }
    }
}*/