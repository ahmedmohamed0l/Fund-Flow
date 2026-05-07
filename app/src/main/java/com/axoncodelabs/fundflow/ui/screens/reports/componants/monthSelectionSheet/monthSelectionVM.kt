package com.axoncodelabs.fundflow.ui.screens.reports.componants.monthSelectionSheet

import androidx.lifecycle.ViewModel
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MonthSelectionVM @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {
    val dates = repository.getAllDates()
}