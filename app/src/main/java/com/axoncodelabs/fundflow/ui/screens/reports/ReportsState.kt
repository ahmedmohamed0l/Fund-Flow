package com.axoncodelabs.fundflow.ui.screens.reports

import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund

sealed class ReportsSheets {
    object None : ReportsSheets()
    object FundSelection : ReportsSheets()
    object DateSelection : ReportsSheets()
    data class EditTransaction(val transaction: TransactionWithFund) : ReportsSheets()
}

data class QueryFilter(
    val fund: FundEntity?,
    val startDate: Long?,
    val endDate: Long?
)

enum class ReportType {
    Expenses,
    Income
}

data class ReportsState(
    val currentSheet: ReportsSheets = ReportsSheets.None,

    val selectedFund: FundEntity? = null,
    val isSelectAllFunds: Boolean = true,

    val selectedDate: Long? = null,
    val isSelectAllDates: Boolean = true,
    val isSelectCurrentMonth: Boolean = false,

    val selectedReportType: ReportType = ReportType.Expenses,
    val exceptTransfers: Boolean = false,

    val expensesSum: Double = 0.0,
    val incomeSum: Double = 0.0,

    val expensesList: List<TransactionWithFund> = emptyList(),
    val incomeList: List<TransactionWithFund> = emptyList(),

    val isHideData: Boolean = false,
)