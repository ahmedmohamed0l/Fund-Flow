package com.axoncodelabs.cashbox.ui.screens.reports

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund

sealed class ReportsEvent {
    // Sheet
    data class SheetDisplayed(val sheet: ReportsSheets) : ReportsEvent()
    object CloseSheet : ReportsEvent()

    // Popup
    data class PopupDisplayed(val popup: ReportsPopups) : ReportsEvent()
    object ClosePopup : ReportsEvent()

    data class OnFundChanged(val fund: FundEntity) : ReportsEvent()
    object OnSelectAllFunds : ReportsEvent()

    data class OnDateChange(val date: Long?) : ReportsEvent()
    object OnSelectAllDates : ReportsEvent()

    data class OnReportTypeChange(val reportType: ReportType) : ReportsEvent()

//    data class OnTransactionClick(val transaction: TransactionWithFund) : ReportsEvent()
}