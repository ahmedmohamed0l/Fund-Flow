package com.axoncodelabs.fundflow.ui.components.sheets

sealed interface SheetResult {
    data object Added : SheetResult
    data object Updated : SheetResult
    data object Deleted : SheetResult
    data object Cancelled : SheetResult
}