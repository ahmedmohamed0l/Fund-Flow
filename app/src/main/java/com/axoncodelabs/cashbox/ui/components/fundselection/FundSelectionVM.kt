package com.axoncodelabs.cashbox.ui.components.fundselection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FundSelectionVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
    val funds = repository.getAllFunds()
    var showFunds by mutableStateOf(false)
}