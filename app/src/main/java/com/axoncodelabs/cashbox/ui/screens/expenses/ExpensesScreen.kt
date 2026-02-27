package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.relation.ExpenseWithFund
import com.axoncodelabs.cashbox.data.util.doubleFormat
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.MyBlurredButton
import com.axoncodelabs.cashbox.ui.components.topAppBar.TopBarState
import com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpense.AddExpenseSheet
import com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.editExpense.EditExpenseSheet
import com.axoncodelabs.cashbox.ui.theme.AppCurrency
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel(),
    onTopBarChange: (TopBarState) -> Unit
) {
    //.....( State & ViewModel Setup ).....
    val state = viewModel.state.collectAsState().value
    val expenses = state.expenses
    val expensesTotalValue = state.expensesTotalValue
    val selectedDate = state.selectedDate

    val sheet = state.currentSheet
    val isHideData = state.isHideData

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    //.....( TopAppBar Data ).....
    LaunchedEffect(Unit) {
        onTopBarChange(
            TopBarState(titleRes = R.string.ExpensesScreen_Identifier)
        )
    }

    //.....( Date Picker Helper ).....
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    //.....( Sheets & Popups Handling ).....
    @Composable
    fun sheetsHandle() {
        when (sheet) {
            ExpensesSheets.AddExpense -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ExpensesEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    AddExpenseSheet(
                        isHideData = isHideData,
                        selectedDate = state.selectedDate,
                        onClose = { viewModel.onEvent(ExpensesEvent.CloseSheet) }
                    )
                }
            }
            is ExpensesSheets.EditExpense -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ExpensesEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    EditExpenseSheet(
                        isHideData = isHideData,
                        expense = sheet.expense,
                        onClose = { viewModel.onEvent(ExpensesEvent.CloseSheet) }
                    )
                }
            }
            ExpensesSheets.None -> Unit
        }
    }
    sheetsHandle()

    //.....( Screen Layout ).....
    ExpensesScreenRoot(
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
        onEvent = viewModel::onEvent
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

    isHideData: Boolean,
    expensesTotalValue: Double,

    expenses: List<ExpenseWithFund>,
    onEvent: (ExpensesEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
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

                ExpensesPageState(
                    expenses = expenses,
                    isHideData = isHideData,
                    selectedDate = selectedDate,
                    onEvent = onEvent
                )
            }

            MyBlurredButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 70.dp)
                    .padding(horizontal = 40.dp),
                text = stringResource(R.string.ExpensesScreen_AddExpenses_Butt),
                onClick = {
                    onEvent(ExpensesEvent.SheetDisplayed(ExpensesSheets.AddExpense))
                }
            )
        }
    }
}

/** --------------------[ Components ]-------------------- **/
/**.....( Date Pick ).....**/
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

/**.....( Screen Components ).....**/
@Composable
private fun ExpensesPageState(
    expenses: List<ExpenseWithFund>,
    isHideData: Boolean,
    selectedDate: Long,
    onEvent: (ExpensesEvent) -> Unit,

    ) {
    if (expenses == emptyList()) {
        EmptyExpensesPage()
    } else {
        ExpensesList(
            expenses = expenses,
            isHideData = isHideData,
            selectedDate = selectedDate,
            onEvent = onEvent
        )
    }
}


@Composable
private fun EmptyExpensesPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.ExpensesScreen_EmptyExpensesPage_Title),
            style = MyFontStyle.largeBold(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 28.sp
        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .scale(1.1f)
                .size(280.dp),
            painter = painterResource(id = R.drawable.img_comfort),
            contentDescription = "comfort"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = stringResource(id = R.string.ExpensesScreen_EmptyExpensesPage_Description),
            style = MyFontStyle.medium(),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun ExpensesList(
    expenses: List<ExpenseWithFund>,
    isHideData: Boolean,
    selectedDate: Long,
    onEvent: (ExpensesEvent) -> Unit,
) {
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
                .padding(horizontal = 20.dp),
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
                    onEvent = onEvent
                )
                if (index != expenses.lastIndex) {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            item { Spacer(modifier = Modifier.height(75.dp)) }
        }
    }
}

@Composable
fun ExpenseItem(
    isHideData: Boolean,
    expense: ExpenseWithFund,
    selectedDate: Long,
    modifier: Modifier = Modifier,
    onEvent: (ExpensesEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(MyRoundedCornerShape.medium)
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                onEvent(ExpensesEvent.SheetDisplayed(ExpensesSheets.EditExpense(expense)))
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
                modifier = Modifier.padding(end = 10.dp),
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
            Box(modifier = Modifier.weight(20f), contentAlignment = Alignment.CenterStart) {
                HideTextData(
                    isHideData = isHideData,
                    text = expense.transaction.description,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MyFontStyle.large()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.weight(10f), contentAlignment = Alignment.CenterEnd) {
                HideTextData(
                    isHideData = isHideData,
                    text = doubleFormat(expense.transaction.amount),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MyFontStyle.extraLargeBold()
                )
            }
        }
    }
}

/** --------------------[ Helpers ]-------------------- **/
fun formatDate(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val sdf = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.forLanguageTag("ar"))
    return sdf.format(cal.time)
}
/*
/**--------------------[ Preview ]--------------------**/
@SuppressLint("RememberReturnType")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val mockExpenseList = mockExpenseList(1)

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = true
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            ExpensesScreenRoot(
                hazeState = remember { HazeState() }, emptyListHazeState = remember { HazeState() },
                isDatePickerOpen = false,
                datePickerState = rememberDatePickerState(),
                onDismissDatePicker = {},
                onClickConfirmBttn = {},

                onPreviousDayClick = {},
                onNextDayClick = {},
                onToggleDatePicker = {},
                selectedDate = System.currentTimeMillis(),

                isHideData = false,
                expensesTotalValue = 0.0, expenses = mockExpenseList
            )
        }
    }
}

private fun mockExpenseList(count: Int): List<ExpenseWithFund> {
    return List(count) { index ->
        ExpenseWithFund(
            transaction = TransactionEntity(
                id = index + 1,
                amount = 100.0 + index * 10,
                description = "مصروف رقم ${index + 1}",
                type = TransactionType.EXPENSE,
                fundId = 1
            ), fund = FundEntity(
                id = 1, name = "صندوق البيت", balance = 0.0
            )
        )
    }
}*/