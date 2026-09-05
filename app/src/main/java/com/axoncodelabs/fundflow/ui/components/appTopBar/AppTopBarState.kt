package com.axoncodelabs.fundflow.ui.components.appTopBar

data class AppTopBarState(
    val titleResId: Int,
    val showAction: Boolean = false,
    val barStartActionIconRes: Int? = null,
    val onBarStartActionClick: (() -> Unit)? = null,
    val barEndActionIconRes: Int? = null,
    val onBarEndActionClick: (() -> Unit)? = null
)