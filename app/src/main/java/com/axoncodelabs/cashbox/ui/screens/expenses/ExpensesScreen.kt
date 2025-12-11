package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value

    fun formatDate(timestamp: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val sdf = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.forLanguageTag("ar"))
        return sdf.format(cal.time)
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.selectedDate
    )

    //DatePicker
    if (state.isDatePickerOpen) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(ExpensesEvent.OnToggleDatePicker) },
            confirmButton = {
                TextButton(onClick = {
                    val selected = datePickerState.selectedDateMillis
                    if (selected != null) {
                        viewModel.onEvent(ExpensesEvent.OnDateSelected(selected))
                    }
                }) { Text(stringResource(R.string.Popups_DatePickerConfirm_Bttn)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(ExpensesEvent.OnToggleDatePicker)
                }) { Text(stringResource(R.string.Popups_Cancel_Bttn)) }
            }
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

    //DatePickerStep-[9]
    Row {
        IconButton(onClick = { viewModel.onEvent(ExpensesEvent.OnPreviousDayClick) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Row(
            modifier = Modifier.clickable {
                viewModel.onEvent(ExpensesEvent.OnToggleDatePicker)
            }
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null)
            Text(formatDate(state.selectedDate))
        }

        IconButton(onClick = { viewModel.onEvent(ExpensesEvent.OnNextDayClick) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }

//    ExpensesScreenRoot()
}

@Composable
private fun ExpensesScreenRoot() {
    Scaffold(
        topBar = {
            MyTopAppBar(title = stringResource(id = R.string.ExpensesScreen_Identifier))
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}