package com.axoncodelabs.fundflow.ui.components.sheets.fundSelection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.axoncodelabs.fundflow.data.repository.FundFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FundSelectionVM @Inject constructor(
    private val repository: FundFlowRepository,
) : ViewModel() {
    val funds = repository.getAllFunds()
    var showFunds by mutableStateOf(false)
}