package com.axoncodelabs.cashbox.ui.screens.reports

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund

sealed class ReportsSheets {
    object None : ReportsSheets()
    object FundSelection : ReportsSheets()
    object DateSelection : ReportsSheets()
    data class EditTransaction(val transaction: TransactionWithFund) : ReportsSheets()
}

sealed class ReportsPopups {
    object None : ReportsPopups()
//    object DateSelection : ReportsPopups()
}

enum class ReportType {
    Expenses,
    Income
}

data class QueryFilter(
    val fund: FundEntity?,
//    val isSelectAllFunds: Boolean,
    val startDate: Long?,
    val endDate: Long?,
//    val isSelectAllDates: Boolean
)
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

    val selectedFund: FundEntity? = null,
    var isSelectAllFunds: Boolean = true,

    val selectedDate: Long? = null,
    var isSelectAllDates: Boolean = true,

    val selectedReportType: ReportType = ReportType.Expenses,

    val expensesSum: Double = 0.0,
    val incomeSum: Double = 0.0,

    val expensesList: List<TransactionWithFund> = emptyList(),
    val incomeList: List<TransactionWithFund> = emptyList(),

    val isHideData: Boolean = false,
)