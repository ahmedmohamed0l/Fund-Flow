package com.axoncodelabs.fundflow.ui.components.sheets.popups

sealed interface PopupResult {
    data object Deleted : PopupResult
    data object Cancelled : PopupResult
}