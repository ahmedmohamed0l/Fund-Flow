package com.axoncodelabs.cashbox.ui.screens.reports.componants.monthSelectionSheet

import androidx.lifecycle.ViewModel
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MonthSelectionVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
    val dates = repository.getAllDates()
}