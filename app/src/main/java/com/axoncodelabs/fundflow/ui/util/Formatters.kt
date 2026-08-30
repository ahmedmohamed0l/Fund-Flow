package com.axoncodelabs.fundflow.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.util.language.appLocale
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

//──── Double Formater ────
private val formatter = DecimalFormat("#,##0.##;'(-'#,##0.##')'", DecimalFormatSymbols(Locale.US))
fun Double.formatAmount(): String {
    return formatter.format(this)
}

//──── Date Formatters ────
object DateFormates {
    private val englishTag = Locale.US

    val FullDate: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", appLocale)

    val FullNumDate: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("dd-MM-yyyy", appLocale)

    val MonthYear: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("MMMM، yyyy", appLocale)

    val DayMonth: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("EEEE d MMMM", appLocale)

    val Time: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("hh:mm a", appLocale)

    val BackupParser: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmm", englishTag)
}

/** > **To formate date for providing in UI**
 * @param formatType [DateFormates]**/
fun Long.dateFormatter(formatType: DateTimeFormatter): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()
    .format(formatType)

/** > **To formate date for providing in UI** **/
fun String.toMillisFromBackup(): Long? {
    val clean = this.removeSuffix("_a").removeSuffix("_m")
    return runCatching {
        LocalDateTime.parse(clean, DateFormates.BackupParser)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }.getOrNull()
}

/** > **To formate backup date for providing in UI** **/
@Composable
fun Long.backupDateFormater(): String {
    val date = this.dateFormatter(DateFormates.DayMonth)
    val time = this.dateFormatter(DateFormates.Time)
    val currentDate = System.currentTimeMillis().dateFormatter(DateFormates.DayMonth)

    val finalDate = if (date == currentDate) stringResource(R.string.Sheet_Today) else date

    val result = "$finalDate، $time"
    return result
}
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
