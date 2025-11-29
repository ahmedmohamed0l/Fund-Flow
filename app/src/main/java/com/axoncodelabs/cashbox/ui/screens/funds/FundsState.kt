package com.axoncodelabs.cashbox.ui.screens.funds

import com.axoncodelabs.cashbox.data.local.entity.FundEntity

data class FundsState(
    val funds: List<FundEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    // Bottom sheet state
    val isAddEditSheetOpen: Boolean = false,
    val isTransferSheetOpen: Boolean = false,
    val editingFundId: Int? = null, // null -> add new, else edit

    // inputs for sheet
    val sheetNameInput: String = "",
    val sheetAmountInput: String = "", // use String for TextField binding, parse to Long when saving
    val sheetDescriptionInput: String = "",

    // for transfer
    val transferFromId: Int? = null,
    val transferToId: Int? = null,
    val transferAmountInput: String = "",

    // sorting
    val sortByDateDesc: Boolean = true,
)