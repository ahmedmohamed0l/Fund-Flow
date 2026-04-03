package com.axoncodelabs.cashbox.ui.screens.reports

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class ReportsEvent {
    // Sheet
    data class SheetDisplayed(val sheet: ReportsSheets) : ReportsEvent()
    object CloseSheet : ReportsEvent()

    // Popup
    data class PopupDisplayed(val popup: ReportsPopups) : ReportsEvent()
    object ClosePopup : ReportsEvent()

    data class OnFundChanged(val fund: FundEntity) : ReportsEvent()
    data class OnDateChange(val newDate: Long) : ReportsEvent()
//    data class OnReportTypeChange(val reportType: ReportType) : ReportsEvent()

//    data class OnTransactionClick(val transaction: TransactionWithFund) : ReportsEvent()
}