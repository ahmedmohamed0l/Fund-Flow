package com.axoncodelabs.fundflow.ui.components.popups

sealed interface PopupResult {
    data object Deleted : PopupResult
    data object Cancelled : PopupResult
}