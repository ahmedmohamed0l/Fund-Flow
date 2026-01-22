package com.axoncodelabs.cashbox.ui.components.topAppBar

data class TopBarState(
    val titleRes: Int,
    val showAction: Boolean = false,
    val actionIconRes: Int? = null,
    val onActionClick: (() -> Unit)? = null
)