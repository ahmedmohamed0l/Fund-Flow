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

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun Preview() {
    mockExpenseList(5)

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = false
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            //──── Content ────
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            }
        }
    }
}

val mockDate = System.currentTimeMillis()

object Mock {
    var isFull = false

    val fund = FundEntity(
        id = 1,
        name = if (isFull) "معاملة جديدة معاملة جديدة معاملة جديدة معاملة جديدة معاملة جديدة" else "صندوق جديد",
        balance = if (isFull) (-9999999999999.99) else 22.0,
        isExcepted = false
    )

    val transaction = TransactionEntity(
        id = 1,
        amount = if (isFull) (-999999999999.9) else 22.0,
        description = if (isFull) "معاملة جديدة معاملة جديدة معاملة جديدة معاملة جديدة معاملة جديدة" else "معاملة جديدة",
        type = TransactionType.EXPENSE,
        fundId = 1,
        isTransfer = true
    )

    val mockTransactionWithFund = TransactionWithFund(
        fund = fund,
        transaction = transaction
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
            ), fund = Mock.fund
        )
    }
}