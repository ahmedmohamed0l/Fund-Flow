package com.axoncodelabs.cashbox.ui.screens.funds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundsViewModel @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FundsState())
    val uiState: StateFlow<FundsState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FundsEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.getAllFunds().collect { list ->
                _uiState.update { it.copy(funds = list) }
            }
        }
    }

    fun onOpenAddFundSheet() {
        _uiState.update {
            it.copy(
                isAddEditSheetOpen = true,
                editingFundId = null,
                sheetNameInput = "",
                sheetAmountInput = "",
                sheetDescriptionInput = ""
            )
        }
    }

    fun onOpenEditSheet(fundId: Int) {
        viewModelScope.launch {
            val fund = repository.getFundById(fundId)
            fund?.let {
                _uiState.update { st ->
                    st.copy(
                        isAddEditSheetOpen = true,
                        editingFundId = it.id,
                        sheetNameInput = it.name,
                        sheetAmountInput = it.balance.toString(),
                        sheetDescriptionInput = it.description
                    )
                }
            } ?: run {
                _events.emit(FundsEvent.ShowError("الصندوق مش موجود"))
            }
        }
    }
}