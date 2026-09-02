package com.axoncodelabs.fundflow.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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

/** > **Adds thousands separators to the integer part while keeping the original value unchanged**
 *
 * **Params:**
 * - VisualTransformation: Changes how the value is displayed.
 * - OffsetMapping: Keeps cursor and selection positions synchronized between the original and transformed text.
 */
class ThousandsSeparatorVisualTransformation : VisualTransformation {

    // Receives the original text and returns the transformed text.
    override fun filter(text: AnnotatedString): TransformedText {

        // Gets the actual text value and returns it as-is if its empty.
        val raw = text.text
        if (raw.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        // Split the number into integer and decimal parts
        val dotIndex = raw.indexOf('.')
        val intPart = if (dotIndex >= 0) raw.substring(0, dotIndex) else raw
        val fracPart = if (dotIndex >= 0) raw.substring(dotIndex) else ""

        // Get the comma count and their positions, then combine it with the decimal part
        val (groupedInt, commasBeforeIndex) = groupThousands(intPart)
        val formatted = groupedInt + fracPart

        // Mapping between the original and formatted number
        val offsetMapping = object : OffsetMapping {

            // Convert the original position to the formatted position
            override fun originalToTransformed(offset: Int): Int {
                // Keep the position within the original integer part boundaries
                val clamped = offset.coerceIn(0, raw.length)

                // Return the exact formatted position by adding the commas before it.
                // If the position passes the original integer part: return the formatted integer position + the extra decimal position (current position - integer part)
                // Ex: [(Formatted index = 9) + {(Current cursor position = 8) - (Original index = 7)}] = 10
                return if (clamped <= intPart.length) {
                    clamped + commasBeforeIndex[clamped]
                } else {
                    groupedInt.length + (clamped - intPart.length)
                }
            }

            // Convert the formatted position to the original position
            override fun transformedToOriginal(offset: Int): Int {
                // Keep the position within the formatted number boundaries
                val clamped = offset.coerceIn(0, formatted.length)

                // Get the current position without the commas, then make sure it does not exceed the original integer part
                // If the position passes the formatted integer part: return the original integer position + the extra decimal position (current position - formatted integer)
                // Ex: [(Original index = 7) + {(Current cursor position = 10) - (Formatted index = 9)}] = 8
                return if (clamped <= groupedInt.length) {
                    val commas = groupedInt.substring(0, clamped).count { it == ',' }
                    (clamped - commas).coerceAtMost(intPart.length)
                } else {
                    intPart.length + (clamped - groupedInt.length)
                }
            }
        }

        // Return the formatted text with its position mapping
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    // Returns the grouped number string and the comma count before each original position. ({8,601,395} & [0,1,1,1,2,2,2])
    // [0,1,1,1,{2},2,2] → index 5 = 2 commas before it in the result: {8,601,[3]95}
    private fun groupThousands(digits: String): Pair<String, IntArray> {
        val len = digits.length
        val commasBeforeIndex = IntArray(len + 1)
        val sb = StringBuilder()
        var commasSoFar = 0
        for (i in 0 until len) {
            commasBeforeIndex[i] = commasSoFar
            sb.append(digits[i])
            val remaining = len - i - 1
            if (remaining > 0 && remaining % 3 == 0) {
                sb.append(',')
                commasSoFar++
            }
        }
        commasBeforeIndex[len] = commasSoFar
        return sb.toString() to commasBeforeIndex
    }
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
