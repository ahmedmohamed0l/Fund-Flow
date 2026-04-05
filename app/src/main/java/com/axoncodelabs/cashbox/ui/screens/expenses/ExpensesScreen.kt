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
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.data.util.myDoubleFormat
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.MyBlurredButton
import com.axoncodelabs.cashbox.ui.components.editTransaction.EditTransactionSheet
import com.axoncodelabs.cashbox.ui.components.topAppBar.TopBarState
import com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpense.AddExpenseSheet
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
                    EditTransactionSheet(
                        isHideData = isHideData,
                        transaction = sheet.expense,
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
        initialDate = selectedDate,
        onDatePickerDismiss = { viewModel.onEvent(ExpensesEvent.OnToggleDatePicker) },
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
    initialDate: Long,
    onDatePickerDismiss: () -> Unit,
    onClickConfirmBttn: (Long) -> Unit,

    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onToggleDatePicker: () -> Unit,
    selectedDate: Long,

    isHideData: Boolean,
    expensesTotalValue: Double,

    expenses: List<TransactionWithFund>,
    onEvent: (ExpensesEvent) -> Unit,
) {
    MyDatePickerDialog(
        isDatePickerOpen = isDatePickerOpen,
        initialDate = initialDate,
        onDatePickerDismiss = { onDatePickerDismiss() },
        onConfirmBttnClick = onClickConfirmBttn,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

/** --------------------[ Components ]-------------------- **/
/**.....( Date Pick ).....**/
@Composable
fun MyDatePickerDialog(
    isDatePickerOpen: Boolean,
    initialDate: Long,
    onDatePickerDismiss: () -> Unit,
    onConfirmBttnClick: (Long) -> Unit,
) {
    if (isDatePickerOpen) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)
        DatePickerDialog(
            onDismissRequest = { onDatePickerDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate ->
                        onConfirmBttnClick(
                            selectedDate
                        )
                    }
                }) {
                    Text(
                        stringResource(R.string.Popups_DatePickerConfirm_Bttn),
                        style = MyFontStyle.small(),
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onConfirmBttnClick(System.currentTimeMillis()) }) {
                    Text(
                        stringResource(R.string.DatePickerTodaySelect),
                        modifier = Modifier
                            .border(
                                shape = CircleShape,
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            .padding(10.dp),
                        style = MyFontStyle.small()
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                TextButton(onClick = { onDatePickerDismiss() }) {
                    Text(
                        stringResource(R.string.Cancel_Bttn),
                        style = MyFontStyle.small(),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
            )
        ) {

            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    headlineContentColor = MaterialTheme.colorScheme.primary,
                    dividerColor = MaterialTheme.colorScheme.primary,
                    weekdayContentColor = MaterialTheme.colorScheme.primary,
                    dayContentColor = MaterialTheme.colorScheme.onBackground,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledDayContentColor = MaterialTheme.colorScheme.outline,
                    todayContentColor = MaterialTheme.colorScheme.primary
                ),
            )
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
            text = (myDoubleFormat(expensesTotalValue)),
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
    expenses: List<TransactionWithFund>,
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
    expenses: List<TransactionWithFund>,
    isHideData: Boolean,
    selectedDate: Long,
    onEvent: (ExpensesEvent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .border(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                shape = MyRoundedCornerShape.extraLarge,
                width = (0.5).dp
            ),
        shape = MyRoundedCornerShape.extraLarge,
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
            item { Spacer(modifier = Modifier.height(125.dp)) }
        }
    }
}

@Composable
fun ExpenseItem(
    isHideData: Boolean,
    expense: TransactionWithFund,
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
                    text = myDoubleFormat(expense.transaction.amount),
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