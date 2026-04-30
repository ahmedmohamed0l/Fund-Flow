package com.axoncodelabs.cashbox.ui.util

import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

//──── Double Formater ────
private val formatter = DecimalFormat("#,##0.##;'(-'#,##0.##')'")
fun Double.formatAmount(): String {
    return formatter.format(this)
}

//──── Double Formatters ────

private val ARABIC_FORMATTER =
    DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.forLanguageTag("ar"))

fun Long.dateFormatter(): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()
    .format(ARABIC_FORMATTER)

// ────────────────{ For (Expenses) }────────────────

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