package com.axoncodelabs.cashbox.ui.screens.funds.components.deletepopup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletePopupVM @Inject constructor(
    private val repository: CashBoxRepository,
) : ViewModel() {
    var fund by mutableStateOf<FundEntity?>(null)
        private set

    fun initData(fund: FundEntity) {
        //Set new
        this.fund = fund
    }

    private val _fundsEvent = Channel<FundsEvent>()
    val fundsEvent = _fundsEvent.receiveAsFlow()

    fun onEvent(event: DeletePopupEvent) {
        when (event) {
            DeletePopupEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    fund?.let {
                        repository.deleteFund(it)
                        repository.deleteAllFundTransactions(it.id)
                    }
                    sendFundsEvent(FundsEvent.ClosePopup)
                }
            }

            DeletePopupEvent.OnCancelClick -> {
                sendFundsEvent(FundsEvent.ClosePopup)
            }
        }
    }

    private fun sendFundsEvent(event: FundsEvent) {
        viewModelScope.launch {
            _fundsEvent.send(event)
        }
    }
}