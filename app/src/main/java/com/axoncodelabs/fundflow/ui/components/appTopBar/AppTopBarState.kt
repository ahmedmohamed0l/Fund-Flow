package com.axoncodelabs.fundflow.ui.components.appTopBar

data class AppTopBarState(
    val titleRes: Int,
    val showAction: Boolean = false,
    val actionIconRes: Int? = null,
    val onActionClick: (() -> Unit)? = null
)