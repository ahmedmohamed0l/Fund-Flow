package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ExpensesViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ExpensesState())
    val state = _state.asStateFlow()

    private val selectedDateFlow = state
        .map { it.selectedDate }
        .distinctUntilChanged()

    private val expensesFlow = selectedDateFlow
        .flatMapLatest { date ->
            repository.getExpensesByDateAndType(
                startDate = date.startOfDay(),
                endDate = date.endOfDay()
            )
        }

    private val expensesSumFlow = selectedDateFlow
        .flatMapLatest { date ->
            repository.getExpensesSumByDateAndType(
                startDate = date.startOfDay(),
                endDate = date.endOfDay()
            )
        }

    init {
        combine(
            expensesFlow,
            expensesSumFlow,
            repository.hideDataFlow,
            selectedDateFlow
        ) { expenses, total, isHideData, selectedDate ->

            _state.value.copy(
                expenses = expenses,
                expensesTotalValue = total,
                isHideData = isHideData,
                selectedDate = selectedDate
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }


    fun changeDay(offset: Int) {
        _state.update {
            it.copy(
                selectedDate = it.selectedDate + offset * 24 * 60 * 60 * 1000
            )
        }
    }

    fun onEvent(event: ExpensesEvent) {
        when (event) {
            ExpensesEvent.OnPreviousDayClick -> changeDay(-1)
            ExpensesEvent.OnNextDayClick -> changeDay(1)
            ExpensesEvent.OnToggleDatePicker -> {
                _state.update {
                    it.copy(isDatePickerOpen = !it.isDatePickerOpen)
                }
            }

            is ExpensesEvent.SheetDisplayed -> {
                _state.update {
                    it.copy(currentSheet = event.sheet)
                }
            }

            ExpensesEvent.CloseSheet -> {
                _state.update {
                    it.copy(currentSheet = ExpensesSheets.None)
                }
            }

            is ExpensesEvent.OnDateSelected -> {
                _state.update {
                    it.copy(
                        selectedDate = event.date,
                        isDatePickerOpen = false
                    )
                }
            }

            is ExpensesEvent.OnExpenseClick -> {}

            ExpensesEvent.OnAddExpense -> {}

            is ExpensesEvent.PopupDisplay -> {
                _state.update {
                    it.copy(popupState = event.popup)
                }
            }

            ExpensesEvent.ClosePopup -> {
                _state.update {
                    it.copy(popupState = ExpensesPopup.Close)
                }
            }
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