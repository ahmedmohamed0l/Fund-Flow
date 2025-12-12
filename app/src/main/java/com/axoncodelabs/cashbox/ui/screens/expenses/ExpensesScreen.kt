package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    //.....( State & ViewModel Setup ).....
    val state = viewModel.state.collectAsState().value

    //.....( Date Picker Helper ).....
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = state.selectedDate)


    //.....( Sheets & Popups Handling ).....

    //.....( Screen Layout ).....
    ExpensesScreenRoot(
        isDatePickerOpen = state.isDatePickerOpen,
        datePickerState = datePickerState,
        onDismissDatePicker = { viewModel.onEvent(ExpensesEvent.OnToggleDatePicker) },
        onClickConfirmBttn = { viewModel.onEvent(ExpensesEvent.OnDateSelected(it)) },

        onPreviousDayClick = { viewModel.onEvent(ExpensesEvent.OnPreviousDayClick) },
        onNextDayClick = { viewModel.onEvent(ExpensesEvent.OnNextDayClick) },
        onToggleDatePicker = { viewModel.onEvent(ExpensesEvent.OnToggleDatePicker) },
        selectedDate = state.selectedDate
    )
}

/**.....( Screen Layout ).....**/
@Composable
private fun ExpensesScreenRoot(
    isDatePickerOpen: Boolean,
    datePickerState: DatePickerState,
    onDismissDatePicker: () -> Unit,
    onClickConfirmBttn: (Long) -> Unit,

    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onToggleDatePicker: () -> Unit,
    selectedDate: Long,
) {
    Scaffold(
        topBar = {
            MyTopAppBar(title = stringResource(id = R.string.ExpensesScreen_Identifier))
        }
    ) { inner ->
        DatePickerDialog(
            isDatePickerOpen = isDatePickerOpen,
            datePickerState = datePickerState,
            onDatePickerDismiss = { onDismissDatePicker() },
            onConfirmBttnClick = onClickConfirmBttn,
        )
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DateSelect(
                onPreviousDayClick = { onPreviousDayClick() },
                onNextDayClick = { onNextDayClick() },
                onToggleDatePicker = { onToggleDatePicker() },
                selectedDate = selectedDate,
            )
        }
    }
}

/** --------------------[ Components ]-------------------- **/
@Composable
fun DatePickerDialog(
    isDatePickerOpen: Boolean,
    datePickerState: DatePickerState,
    onDatePickerDismiss: () -> Unit,
    onConfirmBttnClick: (Long) -> Unit,
) {
    if (isDatePickerOpen) {
        DatePickerDialog(
            onDismissRequest = { onDatePickerDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    val selected = datePickerState.selectedDateMillis
                    if (selected != null) {
                        onConfirmBttnClick(selected)
                    }
                }) {
                    Text(stringResource(R.string.Popups_DatePickerConfirm_Bttn))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDatePickerDismiss }) { Text(stringResource(R.string.Popups_Cancel_Bttn)) }
            }
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

}

@Composable
fun DateSelect(
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onToggleDatePicker: () -> Unit,
    selectedDate: Long,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, bottom = 25.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyIcons.Arrow(
            autoMirroredState = true,
            angle = 180f,
            size = 25.dp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.clickable { onPreviousDayClick() }
        )
        Spacer(modifier = Modifier.width(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onToggleDatePicker() }
        ) {
            Text(
                text = (stringResource(R.string.ExpensesScreen_DateSelect) +
                        " " + formatDate(selectedDate)),
                style = MyFontStyle.medium(),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(5.dp))
            MyIcons.Calendar(
                size = 27.dp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.width(5.dp))

        MyIcons.Arrow(
            autoMirroredState = true,
            size = 25.dp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.clickable { onNextDayClick() }
        )
    }
}

/** --------------------[ Tiny Composables ]-------------------- **/

/** --------------------[ Helpers ]-------------------- **/
fun formatDate(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val sdf = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.forLanguageTag("ar"))
    return sdf.format(cal.time)
}