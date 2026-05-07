package com.axoncodelabs.fundflow.ui.screens.reports

import com.axoncodelabs.fundflow.data.local.entity.FundEntity

sealed class ReportsEvent {
    // Sheet
    data class SheetDisplayed(val sheet: ReportsSheets) : ReportsEvent()
    object CloseSheet : ReportsEvent()

    data class OnFundChanged(val fund: FundEntity) : ReportsEvent()
    object OnSelectAllFunds : ReportsEvent()

    data class OnDateChange(val date: Long?) : ReportsEvent()
    object OnSelectAllDates : ReportsEvent()
    object OnSelectCurrentMonth : ReportsEvent()

    object OnToggleTransfers : ReportsEvent()
    data class OnReportTypeChange(val reportType: ReportType) : ReportsEvent()
}