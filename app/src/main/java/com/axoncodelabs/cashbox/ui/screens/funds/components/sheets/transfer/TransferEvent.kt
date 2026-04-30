package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.transfer

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class TransferEvent {
    data class OnToFundSelected(val fund: FundEntity) : TransferEvent()
    data class OnAmountChange(val amount: String) : TransferEvent()
    data class OnDescriptionChange(val description: String) : TransferEvent()
    data class OnDateChange(val newDate: Long) : TransferEvent()
    object OnSaveClick : TransferEvent()
}