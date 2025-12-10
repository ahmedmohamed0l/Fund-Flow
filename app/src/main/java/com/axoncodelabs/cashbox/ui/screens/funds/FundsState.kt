package com.axoncodelabs.cashbox.ui.screens.funds

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

sealed class FundsSheets {
    object None : FundsSheets()
    object AddFund : FundsSheets()
    data class FundOptions(val fund: FundEntity) : FundsSheets()
    data class AddAmount(val fund: FundEntity) : FundsSheets()
    data class Transfer(val fund: FundEntity) : FundsSheets()
}

sealed class FundsPopup {
    data class DeleteFund(val fund: FundEntity) : FundsPopup()
    object Close : FundsPopup()
}

data class FundsState(
    val currentSheet: FundsSheets = FundsSheets.None,
    val popupState: FundsPopup = FundsPopup.Close,
    val funds: List<FundEntity> = emptyList(),
    val isHideData: Boolean? = false,
)