package com.axoncodelabs.cashbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme

@Preview(showBackground = true)
@Composable
private fun Preview() {
    /* Mock Data Count
    val mockExpenseList = mockExpenseList(1)*/

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = false
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            /**--------- Content ---------**/
        }
    }
}
/*  Mock Data
private fun mockExpenseList(count: Int): List<ExpenseWithFund> {
    return List(count) { index ->
        ExpenseWithFund(
            transaction = TransactionEntity(
                id = index + 1,
                amount = 100.0 + index * 10,
                description = "مصروف رقم ${index + 1}",
                type = TransactionType.EXPENSE,
                fundId = 1
            ), fund = FundEntity(
                id = 1, name = "صندوق البيت", balance = 0.0
            )
        )
    }
}*/