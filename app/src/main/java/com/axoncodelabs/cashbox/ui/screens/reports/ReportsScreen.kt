package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.compose.runtime.Composable

@Composable
fun ReportsScreen() {
}
/*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.EditExpenseDialog
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar
import com.axoncodelabs.cashbox.ui.components.ReportCard

@Composable
fun ReportScreen(viewModel: ReportViewModel = hiltViewModel()) {
    val state = viewModel.state.value

    Column(modifier = Modifier.fillMaxSize()) {
        state.dailyExpenses.forEach { daily ->
            ReportCard(
                dailyExpense = daily,
                isExpanded = state.expandedDays.contains(daily.date),
                onToggle = { viewModel.onEvent(ReportScreenEvent.ToggleDayCard(daily.date)) },
                onSelectExpense = { viewModel.onEvent(ReportScreenEvent.SelectExpense(it)) }
            )
        }

        state.selectedExpense?.let { expense ->
            if(state.showEditDialog) {
                EditExpenseDialog(
                    expense = expense,
                    onDismiss = { viewModel.onEvent(ReportScreenEvent.DismissDialog) },
                    onSave = { updated ->
                        viewModel.onEvent(ReportScreenEvent.DismissDialog)
                        viewModelScope.launch { viewModel.repository.updateTransaction(updated) }
                    }
                )
            }
        }
    }
}*/