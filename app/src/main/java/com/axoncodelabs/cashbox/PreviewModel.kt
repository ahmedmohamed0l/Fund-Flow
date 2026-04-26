package com.axoncodelabs.cashbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionType
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
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
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {

            }
        }
    }
}

object Full {
    val mockFund = FundEntity(
        id = 1,
        name = "صندوق جديد صندوق جديد صندوق جديد صندوق جديد صندوق جديد",
        balance = (-9999999999999.99),
        isExcepted = false
    )

    val mockTransaction = TransactionEntity(
        id = 1,
        amount = (-999999999999.9),
        description = "معاملة جديدة معاملة جديدة معاملة جديدة معاملة جديدة معاملة جديدة",
        type = TransactionType.EXPENSE,
        fundId = 1,
        isTransfer = true
    )

    val mockTransactionWithFund = TransactionWithFund(
        transaction = mockTransaction,
        fund = mockFund
    )
}

object Normal {
    val mockFund = FundEntity(
        id = 1,
        name = "صندوق جديد",
        balance = 22.0,
        isExcepted = false
    )

    val mockTransaction = TransactionEntity(
        id = 1,
        amount = 22.0,
        description = "معاملة جديدة",
        type = TransactionType.EXPENSE,
        fundId = 1,
        isTransfer = true
    )

    val mockTransactionWithFund = TransactionWithFund(
        transaction = mockTransaction,
        fund = mockFund
    )
}

private fun mockExpenseList(count: Int): List<TransactionWithFund> {
    return List(count) { index ->
        TransactionWithFund(
            transaction = TransactionEntity(
                id = index + 1,
                amount = 100.0 + index * 10,
                description = "مصروف رقم ${index + 1}",
                type = TransactionType.EXPENSE,
                fundId = 1
            ), fund = Normal.mockFund
        )
    }
}