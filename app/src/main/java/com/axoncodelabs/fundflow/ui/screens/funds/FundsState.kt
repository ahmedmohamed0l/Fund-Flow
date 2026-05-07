package com.axoncodelabs.fundflow.ui.screens.funds

import com.axoncodelabs.fundflow.data.local.entity.FundEntity

sealed class FundsSheets {
    object None : FundsSheets()
    object AddFund : FundsSheets()
    data class FundOptions(val fund: FundEntity) : FundsSheets()
    data class AddAmount(val fund: FundEntity) : FundsSheets()
    data class Transfer(val fund: FundEntity) : FundsSheets()
}

sealed class FundsPopup {
    data class DeleteFund(val fund: FundEntity) : FundsPopup()
    object None : FundsPopup()
}

data class FundsState(
    val currentSheet: FundsSheets = FundsSheets.None,
    val popupState: FundsPopup = FundsPopup.None,

    val isHideData: Boolean = false,

    val funds: List<FundEntity> = emptyList(),
    val fundsTotalBalance: Double = 0.0,
)