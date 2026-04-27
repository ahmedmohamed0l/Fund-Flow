package com.axoncodelabs.cashbox.ui.util

import java.text.DecimalFormat

fun doubleFormat(double: Double): String {
    val formatter = DecimalFormat("#,##0.##;'(-'#,##0.##')'")
    return formatter.format(double)
}