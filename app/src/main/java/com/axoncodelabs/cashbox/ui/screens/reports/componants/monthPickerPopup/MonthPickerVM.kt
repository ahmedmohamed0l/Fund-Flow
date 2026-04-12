package com.axoncodelabs.cashbox.ui.screens.reports.componants.monthPickerPopup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.dao.TransactionDao.DateRange
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.Month
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MonthPickerVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

    fun setInitialDate(dateMillis: Long?) {
        val target = dateMillis?.toYearMonth()
            ?: YearMonth.now()

        _selectedYear.value = target.year

        val months = _availableMonthNumbers.value
        val index = months.indexOf(target.monthValue)

        _selectedMonthIndex.value = if (index != -1) index else 0
    }

    // ── Available ───────────────────────────
    private val _availableYears = MutableStateFlow<List<Int>>(emptyList())
    val availableYears: StateFlow<List<Int>> = _availableYears.asStateFlow()

    private val _availableMonthNumbers = MutableStateFlow<List<Int>>(emptyList())
    val availableMonthNames: StateFlow<List<String>> = _availableMonthNumbers.map { numbers ->
        numbers.toMonthNames(Locale.forLanguageTag("ar"), TextStyle.SHORT)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ── Selected ───────────────────────────
    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear: StateFlow<Int?> = _selectedYear.asStateFlow()

    private val _selectedMonthIndex = MutableStateFlow(0)
    val selectedMonthIndex: StateFlow<Int> = _selectedMonthIndex.asStateFlow()

    // ── Confirmed ───────────────────────────
    private val _confirmedDateMillis = MutableStateFlow<Long?>(null)
    val confirmedDateMillis: StateFlow<Long?> = _confirmedDateMillis.asStateFlow()

    // ── Getter ───────────────────────────
    private val dateRangeFlow: Flow<DateRange> = repository.getFirstAndLastDate()
    private var allYearMonths: List<YearMonth> = emptyList()

    init {
        viewModelScope.launch {
            dateRangeFlow.collect { dateRange ->
                updateDateRange(dateRange)
            }
        }
    }

    private fun updateDateRange(dateRange: DateRange) {
        val first = dateRange.firstDate
        val last = dateRange.lastDate

        if (first == null || last == null) {
            _availableYears.value = emptyList()
            _availableMonthNumbers.value = emptyList()
            _selectedYear.value = null
            allYearMonths = emptyList()
            return
        }

        val start = first.toYearMonth()
        val end = last.toYearMonth()
        allYearMonths = generateYearMonths(start, end)

        val years = allYearMonths.map { it.year }.distinct().sorted()
        _availableYears.value = years

        val currentYear = _selectedYear.value
        val newSelectedYear = if (currentYear != null && years.contains(currentYear)) {
            currentYear
        } else {
            years.firstOrNull()
        }
        _selectedYear.value = newSelectedYear

        updateMonthsForYear(newSelectedYear)
    }

    private fun generateYearMonths(start: YearMonth, end: YearMonth): List<YearMonth> {
        val list = mutableListOf<YearMonth>()
        var current = start
        while (current <= end) {
            list.add(current)
            current = current.plusMonths(1)
        }
        return list
    }

    private fun updateMonthsForYear(year: Int?) {
        if (year == null || allYearMonths.isEmpty()) {
            _availableMonthNumbers.value = emptyList()
            return
        }

        val months = allYearMonths
            .filter { it.year == year }
            .map { it.monthValue }
            .distinct()
            .sorted()

        _availableMonthNumbers.value = months

        val currentIndex = _selectedMonthIndex.value
        if (currentIndex !in months.indices) {
            _selectedMonthIndex.value = 0
        }
    }

    fun onYearSelected(year: Int) {
        _selectedYear.value = year
        updateMonthsForYear(year)
    }

    fun onMonthIndexSelected(index: Int) {
        _selectedMonthIndex.value = index
    }

    fun onConfirmSelection() {
        val year = _selectedYear.value ?: return
        val monthNumbers = _availableMonthNumbers.value
        val index = _selectedMonthIndex.value
        val actualMonth = monthNumbers.getOrNull(index) ?: return

        val yearMonth = YearMonth.of(year, actualMonth)
        _confirmedDateMillis.value = yearMonth.toEpochMilli()
    }

    fun clearConfirmedDate() {
        _confirmedDateMillis.value = null
    }
}

// ── Helpers ───────────────────────────
fun Long.toYearMonth(): YearMonth =
    YearMonth.from(Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC))

fun YearMonth.toEpochMilli(): Long =
    this.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

fun List<Int>.toMonthNames(locale: Locale, style: TextStyle = TextStyle.SHORT): List<String> =
    this.map { monthNumber ->
        Month.of(monthNumber).getDisplayName(style, locale)
    }