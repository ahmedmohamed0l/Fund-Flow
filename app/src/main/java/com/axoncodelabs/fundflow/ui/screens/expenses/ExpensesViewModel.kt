package com.axoncodelabs.fundflow.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import com.axoncodelabs.fundflow.ui.util.endOfDay
import com.axoncodelabs.fundflow.ui.util.startOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {

    //── State ──
    private val _state = MutableStateFlow(ExpensesState())
    val state = _state.asStateFlow()

    //──── Data Flows ────
    private val selectedDateFlow = _state
        .map { it.selectedDate }
        .distinctUntilChanged()

    private val expensesWithTotalFlow = selectedDateFlow
        .flatMapLatest { date ->
            repository.getExpensesByDateAndType(
                startDate = date.startOfDay(),
                endDate = date.endOfDay()
            )
                .map { list ->
                    list to list.sumOf { it.transaction.amount }
                }
        }

    //──── Init ────
    init {
        combine(
            expensesWithTotalFlow,
            repository.hideDataFlow,
            selectedDateFlow
        ) { (expenses, total), isHideData, selectedDate ->
            _state.update { currentState ->
                currentState.copy(
                    expenses = expenses,
                    expensesTotalValue = total,
                    isHideData = isHideData,
                    selectedDate = selectedDate
                )
            }
        }.launchIn(viewModelScope)
    }

    //──── Helpers ────
    fun changeDay(offset: Long) {
        _state.update {
            it.copy(
                selectedDate = Instant.ofEpochMilli(it.selectedDate)
                    .plus(offset, ChronoUnit.DAYS)
                    .toEpochMilli()
            )
        }
    }

    fun resetSelectedDate(){
        _state.update {
            it.copy(
                selectedDate = System.currentTimeMillis()
            )
        }
    }

    //──── Events ────
    fun onEvent(event: ExpensesEvent) {
        when (event) {
            //── Sheets ──
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

            //── Popups ──
            is ExpensesEvent.PopupDisplayed -> {
                _state.update {
                    it.copy(currentPopup = event.popup)
                }
            }

            ExpensesEvent.ClosePopup -> {
                _state.update {
                    it.copy(currentPopup = ExpensesPopup.None)
                }
            }

            //── DatePickerEvents ──
            ExpensesEvent.OnPreviousDayClick -> changeDay(-1)
            ExpensesEvent.OnNextDayClick -> changeDay(1)

            is ExpensesEvent.OnDateSelected -> {
                _state.update {
                    it.copy(
                        selectedDate = event.date,
                        currentPopup = ExpensesPopup.None
                    )
                }
            }
        }
    }
}