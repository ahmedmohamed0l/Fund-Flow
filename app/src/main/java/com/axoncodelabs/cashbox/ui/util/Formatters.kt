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

//──── Date Formatters ────
object DateFormates {
    val FullDateArabic: DateTimeFormatter =
        DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.forLanguageTag("ar"))

    val MonthYearArabic: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMMM، yyyy", Locale.forLanguageTag("ar"))
}

/** > **To formate date for providing in UI**
 * @param formatType [DateFormates]**/
fun Long.dateFormatter(formatType: DateTimeFormatter): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()
    .format(formatType)


// ────────────────{ Date Value Formatter for get data }────────────────
// ────────( Day Formatter )────────
/** > **Formate date for get data** **/
fun Long.startOfDay(): Long {
    val cal = Calendar.getInstance().apply { timeInMillis = this@startOfDay }
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

/** > **Formate date for get data** **/
fun Long.endOfDay(): Long {
    val cal = Calendar.getInstance().apply { timeInMillis = this@endOfDay }
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    return cal.timeInMillis
}

// ────────( Month Formatter )────────

/** > **Formate date for get data** **/
fun Long.startOfMonth(): Long {
    val cal = Calendar.getInstance().apply { timeInMillis = this@startOfMonth }
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    return cal.timeInMillis
}

/** > **Formate date for get data** **/
fun Long.endOfMonth(): Long {
    val cal = Calendar.getInstance().apply { timeInMillis = this@endOfMonth }
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    return cal.timeInMillis
}