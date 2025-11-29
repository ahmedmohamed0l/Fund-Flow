package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.lifecycle.ViewModel
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {

}