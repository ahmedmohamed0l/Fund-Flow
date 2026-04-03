package com.axoncodelabs.cashbox.ui.screens.reports

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import java.time.LocalDate

sealed class ReportsSheets {
    object None : ReportsSheets()
    object EditTransaction : ReportsSheets()
    object FundSelection : ReportsSheets()
}

sealed class ReportsPopups {
    object None : ReportsPopups()
    object DateSelection : ReportsPopups()
}

enum class ReportType {
    Expenses,
    Income
}

/*data class DayTransactions(
    val date: Long,
    val dayName: String,
    val formattedDate: String,
    val totalAmount: Double,
    val transactions: List<TransactionWithFund>,
    var isExpanded: Boolean = false
)*/

data class ReportsState(
    val currentSheet: ReportsSheets = ReportsSheets.None,
    val popupState: ReportsPopups = ReportsPopups.None,

    val fundsList: List<FundEntity> = emptyList(),
    val selectedFund: FundEntity? = null,

    val datesList: List<Long> = emptyList(),
    val selectedDate: Long? = null,

    val selectedReportType: ReportType = ReportType.Expenses,
//    val expensesList: List<DayTransactions> = emptyList(),
//    val incomeList: List<DayTransactions> = emptyList(),
//    val expensesSum: Double = 0.0,
//    val incomeSum: Double = 0.0,
    val isHideData: Boolean = false,
    )