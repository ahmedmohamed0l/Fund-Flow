package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.ui.components.AppCurrency
import com.axoncodelabs.cashbox.ui.components.EmptyPage
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.MainBttn
import com.axoncodelabs.cashbox.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.cashbox.ui.components.noRippleClickable
import com.axoncodelabs.cashbox.ui.components.sheets.editTransaction.EditTransactionSheet
import com.axoncodelabs.cashbox.ui.screens.expenses.components.popups.DatePickerPopup
import com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpense.AddExpenseSheet
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.util.DateFormates
import com.axoncodelabs.cashbox.ui.util.dateFormatter
import com.axoncodelabs.cashbox.ui.util.formatAmount

@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel(),
    onTopBarChange: (AppTopBarState) -> Unit
) {
    //──── State & ViewModel Setup ────
    val state by viewModel.state.collectAsState()

    val sheet = state.currentSheet
    val popup = state.currentPopup
    val isHideData = state.isHideData

    val selectedDate = state.selectedDate

    val expenses = state.expenses
    val expensesTotalValue = state.expensesTotalValue

    //──── AppTopBar Data ────
    SideEffect {
        onTopBarChange(AppTopBarState(titleRes = R.string.ExpensesScreen_Identifier))
    }

    //──── Sheets & Popups Handling ────
    SheetsHandler(
        sheet = sheet,
        isHideData = isHideData,
        selectedDate = selectedDate,
        onClose = { viewModel.onEvent(ExpensesEvent.CloseSheet) }
    )

    PopupsHandler(
        popup = popup,
        selectedDate = selectedDate,
        onClose = { viewModel.onEvent(ExpensesEvent.ClosePopup) },
        onDateSelected = { date ->
            viewModel.onEvent(ExpensesEvent.OnDateSelected(date))
        }
    )

    //──── Screen Layout ────
    ExpensesScreenRoot(
        onPreviousDayClick = { viewModel.onEvent(ExpensesEvent.OnPreviousDayClick) },
        onNextDayClick = { viewModel.onEvent(ExpensesEvent.OnNextDayClick) },
        onShowDatePicker = {
            viewModel.onEvent(
                ExpensesEvent.PopupDisplayed(ExpensesPopup.DatePicker(state.selectedDate))
            )
        },
        selectedDate = state.selectedDate,

        isHideData = isHideData,

        expensesTotalValue = expensesTotalValue,

        expenses = expenses,
        onExpenseClick = {
            viewModel.onEvent(
                ExpensesEvent.SheetDisplayed(
                    ExpensesSheets.EditExpense(
                        it
                    )
                )
            )
        },

        onAddExpenseClick = { viewModel.onEvent(ExpensesEvent.SheetDisplayed(ExpensesSheets.AddExpense)) }
    )
}

// ────────────────{ Handlers }────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetsHandler(
    sheet: ExpensesSheets,
    isHideData: Boolean,
    selectedDate: Long,
    onClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (sheet != ExpensesSheets.None) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            containerColor = MaterialTheme.colorScheme.background,
            sheetState = sheetState
        ) {
            when (sheet) {
                ExpensesSheets.AddExpense -> {
                    AddExpenseSheet(
                        isHideData = isHideData,
                        selectedDate = selectedDate,
                        onClose = onClose
                    )
                }

                is ExpensesSheets.EditExpense -> {
                    EditTransactionSheet(
                        isHideData = isHideData,
                        transaction = sheet.expense,
                        onClose = onClose
                    )
                }

                ExpensesSheets.None -> Unit
            }
        }
    }
}

@Composable
private fun PopupsHandler(
    popup: ExpensesPopup,
    selectedDate: Long,
    onClose: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    when (popup) {
        is ExpensesPopup.DatePicker -> {
            DatePickerDialog(
                confirmButton = {},
                onDismissRequest = onClose,
                colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                DatePickerPopup(
                    initialDate = selectedDate,
                    onConfirmBttnClick = onDateSelected,
                    onDatePickerDismiss = onClose,
                )
            }
        }

        ExpensesPopup.None -> Unit
    }
}

// ────────────────{ Screen Layout }────────────────
@Composable
private fun ExpensesScreenRoot(
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onShowDatePicker: () -> Unit,
    selectedDate: Long,

    isHideData: Boolean,

    expensesTotalValue: Double,

    expenses: List<TransactionWithFund>,
    onExpenseClick: (TransactionWithFund) -> Unit,

    onAddExpenseClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ScreenHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                onPreviousDayClick = onPreviousDayClick,
                onNextDayClick = onNextDayClick,
                onShowDatePicker = onShowDatePicker,
                selectedDate = selectedDate,

                emptyExpensesList = expenses.isEmpty(),
                isHideData = isHideData,
                expensesTotalValue = expensesTotalValue
            )

            ExpensesPageState(
                isHideData = isHideData,
                expenses = expenses,
                onExpenseClick = onExpenseClick
            )
        }

        MainBttn(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 70.dp)
                .padding(horizontal = 40.dp),
            clipShape = MyRoundedCornerShape.large,
            text = stringResource(R.string.ExpensesScreen_AddExpenses_Butt),
            onClick = onAddExpenseClick
        )
    }
}

// ────────────────{ Components }────────────────
// ────────( Date Pick )────────
@Composable
private fun ScreenHeader(
    modifier: Modifier = Modifier,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onShowDatePicker: () -> Unit,
    selectedDate: Long,

    emptyExpensesList: Boolean,
    isHideData: Boolean,
    expensesTotalValue: Double,
) {
    Card(
        modifier = modifier,
        shape = MyRoundedCornerShape.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DateSelect(
                onPreviousDayClick = { onPreviousDayClick() },
                onNextDayClick = { onNextDayClick() },
                onToggleDatePicker = { onShowDatePicker() },
                selectedDate = selectedDate,
            )
            AnimatedVisibility(
                visible = !emptyExpensesList,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(durationMillis = 200)
                )
                        + fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = 100)),

                exit = fadeOut(animationSpec = tween(durationMillis = 200))
                        + shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(durationMillis = 200, delayMillis = 100)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                            .padding(vertical = 15.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    DayExpensesTotalValue(
                        isHideData = isHideData,
                        expensesTotalValue = expensesTotalValue
                    )
                }
            }
        }
    }
}

//── Tini Components ──
@Composable
private fun DateSelect(
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onToggleDatePicker: () -> Unit,
    selectedDate: Long,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyIcons.Arrow(
            autoMirroredState = true,
            angle = 180f,
            size = 25.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .offset(y = (-2.5).dp)
                .noRippleClickable { onPreviousDayClick() }
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { onToggleDatePicker() },
            text = (stringResource(R.string.ExpensesScreen_DateSelect)
                    + " "
                    + selectedDate.dateFormatter(DateFormates.FullDateArabic)),
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        MyIcons.Calendar(
            modifier = Modifier
                .offset(y = (-2.5).dp)
                .noRippleClickable { onToggleDatePicker() },
            size = 27.dp,
            color = MaterialTheme.colorScheme.primary,
        )
        MyIcons.Arrow(
            autoMirroredState = true,
            size = 25.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .offset(y = (-2.5).dp)
                .noRippleClickable { onNextDayClick() }
        )
    }
}

@Composable
private fun DayExpensesTotalValue(
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
            text = expensesTotalValue.formatAmount(),
            color = MaterialTheme.colorScheme.primary,
            style = MyFontStyle.largeBold(),
        )
        AppCurrency(textColor = MaterialTheme.colorScheme.primary)
    }
}

// ────────( Expenses List )────────
@Composable
private fun ExpensesPageState(
    isHideData: Boolean,
    expenses: List<TransactionWithFund>,
    onExpenseClick: (TransactionWithFund) -> Unit,
) {
    if (expenses.isEmpty()) {
        EmptyPage(
            modifier = Modifier.fillMaxSize(),
            topText = stringResource(id = R.string.ExpensesScreen_EmptyExpensesPage_Title),
            image = painterResource(id = R.drawable.img_comfort),
            imageScale = 1.1f,
            imageSize = 280.dp,
            bottomText = stringResource(id = R.string.ExpensesScreen_EmptyExpensesPage_Description),
            bottomSpace = 125.dp
        )
    } else {
        ExpensesList(
            isHideData = isHideData,
            expenses = expenses,
            onExpenseClick = onExpenseClick
        )
    }
}

//── Tini Components ──
@Composable
private fun ExpensesList(
    isHideData: Boolean,
    expenses: List<TransactionWithFund>,
    onExpenseClick: (TransactionWithFund) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 125.dp),
    ) {
        itemsIndexed(
            items = expenses,
            key = { _, expense -> expense.transaction.id },
            contentType = { _, _ -> "ExpenseItem" }) { index, expense ->
            ExpenseItem(
                modifier = Modifier.fillMaxWidth(),
                isHideData = isHideData,
                expense = expense,
                onExpenseClick = onExpenseClick
            )
            if (index != expenses.lastIndex) {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    expense: TransactionWithFund,
    onExpenseClick: (TransactionWithFund) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .noRippleClickable { onExpenseClick(expense) }
            .border(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                shape = MyRoundedCornerShape.medium,
                width = (0.5).dp
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(15.dp)
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = expense.transaction.date.dateFormatter(DateFormates.FullDateArabic),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.small()
            )
            HideTextData(
                isHideData = isHideData,
                text = expense.fund.name,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.small(),
                textAlign = TextAlign.End,
                align = Alignment.CenterEnd
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HideTextData(
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 5.dp),
                isHideData = isHideData,
                text = expense.transaction.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.largeXLight(),
                maxLines = 2,
                textAlign = TextAlign.Start,
                align = Alignment.CenterStart
            )
            HideTextData(
                modifier = Modifier.weight(1f),
                isHideData = isHideData,
                text = expense.transaction.amount.formatAmount(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.xxLargeBold(),
                textAlign = TextAlign.End,
                align = Alignment.CenterEnd
            )
            AppCurrency(textColor = MaterialTheme.colorScheme.onBackground)
        }
    }
}