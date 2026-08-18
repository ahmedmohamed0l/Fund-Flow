package com.axoncodelabs.fundflow.ui.util

import java.util.Calendar

/**
 * Adjusts timestamp based on selected date:
 * - Today: returns current time
 * - Past date: returns end of that day (23:59:59.999)
 * - Future date: returns start of that day (00:00:00.000)
 */
fun getAdjustedTime(selectedDate: Long): Long {
    val now = Calendar.getInstance()
    val selectedCal = Calendar.getInstance().apply { timeInMillis = selectedDate }

    val startOfToday = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val isToday = selectedCal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
            selectedCal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)

    if (isToday) {
        return now.timeInMillis
    }

    if (selectedCal.timeInMillis < startOfToday.timeInMillis) {
        selectedCal.set(Calendar.HOUR_OF_DAY, 23)
        selectedCal.set(Calendar.MINUTE, 59)
        selectedCal.set(Calendar.SECOND, 59)
        selectedCal.set(Calendar.MILLISECOND, 999)
        return selectedCal.timeInMillis
    } else {
        selectedCal.set(Calendar.HOUR_OF_DAY, 0)
        selectedCal.set(Calendar.MINUTE, 0)
        selectedCal.set(Calendar.SECOND, 0)
        selectedCal.set(Calendar.MILLISECOND, 0)
        return selectedCal.timeInMillis
    }
}