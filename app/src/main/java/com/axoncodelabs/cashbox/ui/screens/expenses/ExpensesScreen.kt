package com.axoncodelabs.cashbox.ui.screens.expenses

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.relation.ExpenseWithFund
import com.axoncodelabs.cashbox.data.util.doubleFormat
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.MyBlurredButton
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar
import com.axoncodelabs.cashbox.ui.theme.AppCurrency
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    //.....( State & ViewModel Setup ).....
    val state = viewModel.state.collectAsState().value
    val expenses = state.expenses
    val expensesTotalValue = state.expensesTotalValue
    val selectedDate = state.selectedDate

    val sheet = state.currentSheet
    val isHideData = state.isHideData

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val hazeState = remember { HazeState() }

    //.....( Date Picker Helper ).....
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    //.....( Sheets & Popups Handling ).....
    fun sheetsHandle() {
        when (sheet) {
            ExpensesSheets.AddExpense -> {}
            is ExpensesSheets.EditExpense -> {}
            ExpensesSheets.None -> Unit
        }
    }


    //.....( Screen Layout ).....
    ExpensesScreenRoot(
        hazeState = hazeState,

        isDatePickerOpen = state.isDatePickerOpen,
        datePickerState = datePickerState,
        onDismissDatePicker = { viewModel.onEvent(ExpensesEvent.OnToggleDatePicker) },
        onClickConfirmBttn = { viewModel.onEvent(ExpensesEvent.OnDateSelected(it)) },

        onPreviousDayClick = { viewModel.onEvent(ExpensesEvent.OnPreviousDayClick) },
        onNextDayClick = { viewModel.onEvent(ExpensesEvent.OnNextDayClick) },
        onToggleDatePicker = { viewModel.onEvent(ExpensesEvent.OnToggleDatePicker) },
        selectedDate = state.selectedDate,


        isHideData = isHideData,
        expensesTotalValue = expensesTotalValue,

        expenses = expenses,
    )
}

/**.....( Screen Layout ).....**/
@Composable
private fun ExpensesScreenRoot(
    hazeState: HazeState,

    isDatePickerOpen: Boolean,
    datePickerState: DatePickerState,
    onDismissDatePicker: () -> Unit,
    onClickConfirmBttn: (Long) -> Unit,

    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onToggleDatePicker: () -> Unit,
    selectedDate: Long,

    isHideData: Boolean,
    expensesTotalValue: Double,

    expenses: List<ExpenseWithFund>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        MyTopAppBar(title = stringResource(id = R.string.ExpensesScreen_Identifier))

        DatePickerDialog(
            isDatePickerOpen = isDatePickerOpen,
            datePickerState = datePickerState,
            onDatePickerDismiss = { onDismissDatePicker() },
            onConfirmBttnClick = onClickConfirmBttn,
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 20.dp),
                    shape = MyRoundedCornerShape.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 5.dp, vertical = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DateSelect(
                            onPreviousDayClick = { onPreviousDayClick() },
                            onNextDayClick = { onNextDayClick() },
                            onToggleDatePicker = { onToggleDatePicker() },
                            selectedDate = selectedDate,
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .width(280.dp)
                                .padding(15.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        DayExpensesTotalValue(
                            isHideData = isHideData,
                            expensesTotalValue = expensesTotalValue
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                            width = (0.5).dp
                        ),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                            .haze(state = hazeState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                        itemsIndexed(
                            items = expenses,
                            key = { _, expense -> expense.transaction.id },
                            contentType = { _, _ -> "ExpenseItem" }) { index, expense ->
                            ExpenseItem(
                                isHideData = isHideData,
                                selectedDate = selectedDate,
                                expense = expense,
                            )
                            if (index != expenses.lastIndex) {
                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }
                        item { Spacer(modifier = Modifier.height(75.dp)) }
                    }
                }
            }

            MyBlurredButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 70.dp),
                text = stringResource(R.string.ExpensesScreen_AddExpenses_Butt),
                bttnWidth = 250.dp,
                hazeState = hazeState,
                onClick = {}
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
                    datePickerState.selectedDateMillis?.let { selectedDate ->
                        onConfirmBttnClick(selectedDate)

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
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyIcons.Arrow(
            autoMirroredState = true,
            angle = 180f,
            size = 25.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onPreviousDayClick() }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggleDatePicker() }
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
        MyIcons.Arrow(
            autoMirroredState = true,
            size = 25.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onNextDayClick() }
        )
    }
}

@Composable
fun DayExpensesTotalValue(
    isHideData: Boolean,
    expensesTotalValue: Double,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.ExpensesScreen_ExpensesTotal),
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(3.dp))
        HideTextData(
            isHideData = isHideData,
            text = (doubleFormat(expensesTotalValue)),
            color = MaterialTheme.colorScheme.primary,
            style = MyFontStyle.largeBold(),
        )
        Spacer(modifier = Modifier.width(5.dp))
        AppCurrency(textColor = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ExpenseItem(
    isHideData: Boolean,
    expense: ExpenseWithFund,
    selectedDate: Long,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(MyRoundedCornerShape.medium)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                //TODO: onExpenseClick(expense)
            }
            .border(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                shape = MyRoundedCornerShape.medium,
                width = (0.5).dp
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(15.dp)
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatDate(selectedDate),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.small()
            )
            HideTextData(
                isHideData = isHideData,
                text = expense.fund.name,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.small()
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HideTextData(
                isHideData = isHideData,
                text = expense.transaction.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.large()
            )
            HideTextData(
                isHideData = isHideData,
                text = doubleFormat(expense.transaction.amount),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.extraLargeBold()
            )
        }
    }
}

/** --------------------[ Tiny Composables ]-------------------- **/

/** --------------------[ Helpers ]-------------------- **/
fun formatDate(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val sdf = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.forLanguageTag("ar"))
    return sdf.format(cal.time)
}

/**--------------------[ Preview ]--------------------**/
@SuppressLint("RememberReturnType")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = false
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            ExpensesScreenRoot(
                hazeState = remember { HazeState() },
                isDatePickerOpen = false,
                datePickerState = rememberDatePickerState(),
                onDismissDatePicker = {},
                onClickConfirmBttn = {},

                onPreviousDayClick = {},
                onNextDayClick = {},
                onToggleDatePicker = {},
                selectedDate = System.currentTimeMillis(),

                isHideData = false,
                expensesTotalValue = 0.0,
                expenses = emptyList()
            )
            /*ExpenseItem(
                isHideData = false,
                selectedDate = System.currentTimeMillis(),
                expense = ExpenseWithFund(
                    TransactionEntity(
                        amount = 100.0,
                        description = "عملية مصروف 1",
                        type = TransactionType.EXPENSE,
                        fundId = 0,
                    ),
                    FundEntity(
                        name = "اسم الصندوق 1",
                        balance = 200.0,
                    )
                ),
            )*/
        }
    }
}