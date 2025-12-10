package com.axoncodelabs.cashbox.ui.screens.funds

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class FundsSheet {
    object None : FundsSheet()
    object AddFund : FundsSheet()
    data class FundOptions(val fund: FundEntity) : FundsSheet()
    data class AddAmount(val fund: FundEntity) : FundsSheet()
    data class Transfer(val fund: FundEntity) : FundsSheet()
}

sealed class FundsPopup {
    data class DeleteFund(val fund: FundEntity) : FundsPopup()
    object Close : FundsPopup()
}

data class FundsState(
    val currentSheet: FundsSheet = FundsSheet.None,
    val popupState: FundsPopup = FundsPopup.Close,
    val funds: List<FundEntity> = emptyList(),
    val isHideData: Boolean? = false,
)