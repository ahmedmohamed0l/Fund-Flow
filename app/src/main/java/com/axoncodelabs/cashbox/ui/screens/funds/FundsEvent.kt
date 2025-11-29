package com.axoncodelabs.cashbox.ui.screens.funds

sealed class FundsEvent {
    data class ShowMessage(val msg: String) : FundsEvent()
    data class ShowError(val error: String) : FundsEvent()
    data class Navigate(val route: String) : FundsEvent()
    object RequireAtLeastOneFund : FundsEvent()
}