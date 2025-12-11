package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ExpensesState())
    val state = _state.asStateFlow()

    private fun changeDay(offset: Int) {
        val newDate = state.value.selectedDate + offset * 24 * 60 * 60 * 1000
        getExpensesForDate(newDate)
    }

    private fun getExpensesForDate(date: Long) {
        val start = date.startOfDay()
        val end = date.endOfDay()

        repository.getExpensesByDate(start, end).onEach { list ->
            _state.update { it.copy(expenses = list, selectedDate = date) }
        }.launchIn(viewModelScope)
    }


    fun onEvent(event: ExpensesEvent) {
        when (event) {
            is ExpensesEvent.OnPreviousDayClick -> changeDay(-1)
            is ExpensesEvent.OnNextDayClick -> changeDay(1)
            is ExpensesEvent.OnToggleDatePicker -> {
                _state.update { it.copy(isDatePickerOpen = !it.isDatePickerOpen) }
            }

            is ExpensesEvent.OnDateSelected -> {
                _state.update { it.copy(isDatePickerOpen = false) }
                getExpensesForDate(event.date)
            }

            else -> {}
        }
    }

    fun Long.startOfDay(): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = this@startOfDay }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun Long.endOfDay(): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = this@endOfDay }
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}