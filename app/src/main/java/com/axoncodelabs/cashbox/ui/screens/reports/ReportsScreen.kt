package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.ui.components.AppCurrency
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.cashbox.ui.components.editTransaction.EditTransactionSheet
import com.axoncodelabs.cashbox.ui.components.fundselection.FundSelectionSheet
import com.axoncodelabs.cashbox.ui.screens.reports.componants.monthSelectionSheet.MonthSelectionSheet
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.util.doubleFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel(),
    onTopBarChange: (AppTopBarState) -> Unit
) {
    //.....( State & ViewModel Setup ).....
    val state by viewModel.state.collectAsState()
    val sheet = state.currentSheet
//    val popup = state.popupState
    val isHideData = state.isHideData
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val expandedDays = remember { mutableStateMapOf<Long, Boolean>() }
    LaunchedEffect(Unit) {
        viewModel.clearExpandedDaysEvent.collect {
            expandedDays.clear()
        }
    }

    //.....( TopAppBar Data ).....
    LaunchedEffect(Unit) {
        onTopBarChange(
            AppTopBarState(titleRes = R.string.ReportsScreen_Identifier)
        )
    }

    //.....( Sheets & Popups Handling ).....
    @Composable
    fun sheetsHandle() {
        when (sheet) {
            ReportsSheets.FundSelection -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ReportsEvent.CloseSheet) },
                    containerColor = colorScheme.background,
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SelectValueBttn(
                            modifier = Modifier.padding(start = 20.dp),
                            isEnable = state.isSelectAllFunds,
                            lapel = stringResource(R.string.ReportsScreen_SelectAll),
                            onClick = { viewModel.onEvent(ReportsEvent.OnSelectAllFunds) }
                        )
                        FundSelectionSheet(
                            isHideData = isHideData,
                            onSelect = {
                                viewModel.onEvent(ReportsEvent.OnFundChanged(it))
                            }
                        )
                    }
                }
            }

            ReportsSheets.DateSelection -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ReportsEvent.CloseSheet) },
                    containerColor = colorScheme.background,
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SelectValueBttn(
                                modifier = Modifier.padding(start = 20.dp),
                                isEnable = state.isSelectAllDates,
                                lapel = stringResource(R.string.ReportsScreen_SelectAll),
                                onClick = { viewModel.onEvent(ReportsEvent.OnSelectAllDates) }
                            )
                            SelectValueBttn(
                                modifier = Modifier.padding(end = 20.dp),
                                isEnable = state.isSelectCurrentMonth,
                                lapel = stringResource(R.string.ReportsScreen_SelectCurrentMonth),
                                onClick = { viewModel.onEvent(ReportsEvent.OnSelectCurrentMonth) }
                            )
                        }
                        MonthSelectionSheet(
                            onSelect = {
                                viewModel.onEvent(ReportsEvent.OnDateChange(it))
                            }
                        )
                    }
                }
            }

            is ReportsSheets.EditTransaction -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ReportsEvent.CloseSheet) },
                    containerColor = colorScheme.background,
                    sheetState = sheetState
                ) {
                    EditTransactionSheet(
                        isHideData = isHideData,
                        transaction = sheet.transaction,
                        onClose = { viewModel.onEvent(ReportsEvent.CloseSheet) }
                    )
                }
            }

            ReportsSheets.None -> Unit
        }
    }
    sheetsHandle()

    /*@Composable
    fun popupsHandle() {
        when (popup) {
            /*ReportsPopups.DateSelection -> {
                BasicAlertDialog(onDismissRequest = { viewModel.onEvent(ReportsEvent.ClosePopup) }) {
                    Surface(
                        shape = MyRoundedCornerShape.extraLarge,
                        color = colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(25.dp))
                            ScrollableMonthPicker(
                                initialDateMillis = state.selectedDate,
                                onDateConfirmed = { dateMillis ->
                                    viewModel.onEvent(ReportsEvent.OnDateChange(dateMillis))
                                }
                            )

                            Spacer(Modifier.height(15.dp))
                            SelectAllValues(
                                modifier = Modifier
                                    .padding(horizontal = 25.dp)
                                    .padding(bottom = 25.dp),
                                selectedValue = formatDate(state.selectedDate),
                                onSelectAll = { viewModel.onEvent(ReportsEvent.OnSelectAllDates) }
                            )
                        }
                    }
                }
            }*/

            ReportsPopups.None -> Unit
        }
    }
    popupsHandle()*/

    //.....( Screen Layout ).....
    ReportsScreenRoot(
        onFundSelectorClick = { viewModel.onEvent(ReportsEvent.SheetDisplayed(ReportsSheets.FundSelection)) },
        onDateSelectorClick = { viewModel.onEvent(ReportsEvent.SheetDisplayed(ReportsSheets.DateSelection)) },

        selectedFund = state.selectedFund?.name.orEmpty(),
        isSelectAllFunds = state.isSelectAllFunds,

        selectedDate = formatDate(state.selectedDate),
        isSelectAllDates = state.isSelectAllDates,

        exceptTransfers = state.exceptTransfers,
        onExceptClick = { viewModel.onEvent(ReportsEvent.OnToggleTransfers) },

        selectedType = state.selectedReportType,
        onTypeSelected = { viewModel.onEvent(ReportsEvent.OnReportTypeChange(it)) },

        expensesSum = state.expensesSum,
        incomeSum = state.incomeSum,

        expenses = state.expensesList,
        income = state.incomeList,
        expandedDays = expandedDays,

        onToggleDay = { dayStart -> expandedDays[dayStart] = !(expandedDays[dayStart] ?: false) },
        onTransactionClick = {
            viewModel.onEvent(
                ReportsEvent.SheetDisplayed(
                    ReportsSheets.EditTransaction(
                        it
                    )
                )
            )
        },

        isHideData = isHideData
    )
}

/**.....( Screen Layout ).....**/
@Composable
private fun ReportsScreenRoot(
    onFundSelectorClick: () -> Unit,
    selectedFund: String,
    isSelectAllFunds: Boolean,

    onDateSelectorClick: () -> Unit,
    selectedDate: String,
    isSelectAllDates: Boolean,

    exceptTransfers: Boolean,
    onExceptClick: () -> Unit,

    selectedType: ReportType,
    expensesSum: Double,
    incomeSum: Double,
    onTypeSelected: (ReportType) -> Unit,

    expenses: List<TransactionWithFund>,
    income: List<TransactionWithFund>,
    onTransactionClick: (TransactionWithFund) -> Unit,

    expandedDays: MutableMap<Long, Boolean>,
    onToggleDay: (Long) -> Unit,

    isHideData: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(20.dp)) }

        item {
            DataSelectors(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),

                onFundSelectorClick = onFundSelectorClick,
                selectedFund = selectedFund,
                isSelectAllFunds = isSelectAllFunds,

                onDateSelectorClick = onDateSelectorClick,
                selectedDate = selectedDate,
                isSelectAllDates = isSelectAllDates,

                isHideData = isHideData
            )
        }

        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.background)
            ) {
                Spacer(Modifier.height(10.dp))
                TransfersException(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    exceptTransfers = exceptTransfers,
                    onExceptClick = onExceptClick
                )
                Spacer(Modifier.height(10.dp))
                ReportTypeSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .padding(horizontal = 20.dp),
                    selectedType = selectedType,
                    expensesSum = expensesSum,
                    incomeSum = incomeSum,
                    onTypeSelected = onTypeSelected,
                    isHideData = isHideData
                )
                Spacer(Modifier.height(20.dp))
            }
        }

        item {
            ReportsPageState(
                expenses = expenses,
                income = income,
                selectedType = selectedType,
                isHideData = isHideData,
                exceptTransfers = exceptTransfers,
                onTransactionClick = onTransactionClick,
                expandedDays = expandedDays,
                onToggleDay = onToggleDay
            )
        }
    }
}

/** --------------------[ Components ]-------------------- **/
/**.....( Data Selectors ).....**/
@Composable
private fun DataSelectors(
    modifier: Modifier = Modifier,

    onFundSelectorClick: () -> Unit,
    selectedFund: String,
    isSelectAllFunds: Boolean,

    onDateSelectorClick: () -> Unit,
    selectedDate: String,
    isSelectAllDates: Boolean,

    isHideData: Boolean
) {
    Column(
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .border(
                1.dp,
                colorScheme.primary.copy(alpha = 0.5f),
                MyRoundedCornerShape.medium
            )
            .background(colorScheme.surface)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fund Selector
        SelectorBar(
            icon = painterResource(id = R.drawable.ic_credit_card),
            title = stringResource(id = R.string.ReportsScreen_FundSelection),
            openSheet = { onFundSelectorClick() },
            selectedValue = selectedFund,
            isSelectAll = isSelectAllFunds,
            isHideData = isHideData
        )

        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
                .clip(MyRoundedCornerShape.medium),
            thickness = 1.dp,
            color = colorScheme.primary.copy(alpha = 0.5f)
        )

        // Date Selector
        SelectorBar(
            icon = painterResource(id = R.drawable.ic_search_activity),
            title = stringResource(id = R.string.ReportsScreen_ZoneSelection),
            openSheet = { onDateSelectorClick() },
            selectedValue = selectedDate,
            isSelectAll = isSelectAllDates,
            isHideData = isHideData
        )
    }
}

@Composable
private fun SelectorBar(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    openSheet: () -> Unit,
    selectedValue: String,
    isSelectAll: Boolean,
    isHideData: Boolean,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
                .weight(2f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    openSheet()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = icon,
                contentDescription = "Fund Selection",
                modifier = modifier.size(30.dp),
                tint = colorScheme.onBackground,
            )
            Text(
                modifier = modifier.padding(start = 5.dp, top = 4.dp),
                text = title,
                color = colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }

        Row(
            modifier = modifier
                .weight(3f)
                .padding(top = 4.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    openSheet()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            HideTextData(
                modifier = Modifier.weight(1f),
                isHideData = isHideData,
                text = if (isSelectAll) stringResource(R.string.ReportsScreen_AllSelected) else selectedValue,
                color = colorScheme.primary,
                style = MyFontStyle.mediumBold(),
                align = Alignment.CenterEnd
            )
            MyIcons.Arrow(
                modifier = modifier.offset(y = (-2.5).dp),
                autoMirroredState = false,
                angle = 180f,
                size = 20.dp,
                color = colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun SelectValueBttn(
    modifier: Modifier = Modifier,
    isEnable: Boolean,
    lapel: String,
    onClick: () -> Unit
) {
    val isEnable = !isEnable
    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                if (isEnable) onClick()
            }
            .clip(CircleShape)
            .border(
                color = if (isEnable) colorScheme.primary else colorScheme.outline,
                shape = CircleShape,
                width = (0.5).dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(7.dp),
            text = lapel,
            color = if (isEnable) colorScheme.onBackground else colorScheme.outline,
            style = MyFontStyle.xSmall()
        )
    }
}

/**.....( Transfers Exception ).....**/
@Composable
private fun TransfersException(
    modifier: Modifier = Modifier,
    exceptTransfers: Boolean,
    onExceptClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MyRoundedCornerShape.medium)
            .border(
                shape = MyRoundedCornerShape.medium,
                width = 1.dp,
                color = colorScheme.surfaceContainerHigh
            )
            .background(colorScheme.surface)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(17.dp)
                    .offset(y = (-2.5).dp),
                imageVector = Icons.Rounded.SyncAlt,
                contentDescription = "",
                tint = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.ReportsScreen_ExceptTransfer),
                color = colorScheme.onBackground,
                style = MyFontStyle.small(),
            )
        }

        Switch(
            modifier = Modifier
                .size(width = 38.dp, height = 25.dp)
                .scale(0.7f)
                .offset(y = (-0.3).dp),
            checked = exceptTransfers,
            onCheckedChange = { onExceptClick() },
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = colorScheme.surface,
                uncheckedBorderColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.8f),
                uncheckedThumbColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                checkedTrackColor = colorScheme.surfaceContainerHigh
            )
        )
    }
}

/**.....( Report Type Selector ).....**/
@Composable
private fun ReportTypeSelector(
    modifier: Modifier = Modifier,
    selectedType: ReportType,
    expensesSum: Double,
    incomeSum: Double,
    onTypeSelected: (ReportType) -> Unit,
    isHideData: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MyRoundedCornerShape.medium)
            .background(colorScheme.primary)
            .padding(4.dp),
    ) {
        // Expenses
        ReportTab(
            modifier = Modifier.weight(1f),
            title = stringResource(id = R.string.ReportsScreen_Expense_Identifier),
            value = expensesSum,
            isSelected = selectedType == ReportType.Expenses,
            onClick = {
                if (selectedType != ReportType.Expenses) onTypeSelected(ReportType.Expenses)
            },
            isHideData = isHideData
        )

        // Income
        ReportTab(
            modifier = Modifier.weight(1f),
            title = stringResource(id = R.string.ReportsScreen_Income_Identifier),
            value = incomeSum,
            isSelected = selectedType == ReportType.Income,
            onClick = {
                if (selectedType != ReportType.Income) onTypeSelected(ReportType.Income)
            },
            isHideData = isHideData
        )
    }
}

@Composable
private fun ReportTab(
    modifier: Modifier,
    title: String,
    value: Double,
    isSelected: Boolean,
    onClick: () -> Unit,
    isHideData: Boolean
) {
    val tabColor by animateColorAsState(
        targetValue = if (isSelected) colorScheme.surface else colorScheme.primary,
        animationSpec = tween(durationMillis = 300)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) colorScheme.onBackground else colorScheme.onPrimary,
        animationSpec = tween(durationMillis = 300)
    )
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(MyRoundedCornerShape.medium)
            .background(tabColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = textColor,
            style = MyFontStyle.large()
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HideTextData(
                isHideData = isHideData,
                text = doubleFormat(value),
                color = textColor,
                style = MyFontStyle.large()
            )
            AppCurrency(textColor = textColor)
        }
    }
}

/**.....( Screen Components ).....**/
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReportsPageState(
    expenses: List<TransactionWithFund>,
    income: List<TransactionWithFund>,
    selectedType: ReportType,
    onTransactionClick: (TransactionWithFund) -> Unit,
    isHideData: Boolean,
    exceptTransfers: Boolean,
    expandedDays: MutableMap<Long, Boolean>,
    onToggleDay: (Long) -> Unit
) {
    AnimatedContent(
        targetState = selectedType,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "ReportsPageTransition"
    ) { selectedType ->
        when (selectedType) {
            ReportType.Expenses -> {
                if (expenses.isEmpty()) EmptyPage()
                else {
                    TransactionsList(
                        transactionsList = expenses,
                        isHideData = isHideData,
                        exceptTransfers = exceptTransfers,
                        onTransactionClick = onTransactionClick,
                        expandedDays = expandedDays,
                        onToggleDay = onToggleDay
                    )
                }
            }

            ReportType.Income -> {
                if (income.isEmpty()) EmptyPage()
                else {
                    TransactionsList(
                        transactionsList = income,
                        isHideData = isHideData,
                        exceptTransfers = exceptTransfers,
                        onTransactionClick = onTransactionClick,
                        expandedDays = expandedDays,
                        onToggleDay = onToggleDay
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .scale(1.1f)
                .size(210.dp),
            painter = painterResource(id = R.drawable.img_reports_noreports),
            contentDescription = "comfort"
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.ReportsScreen_NoReports_Title),
            style = MyFontStyle.largeBold(),
            textAlign = TextAlign.Center,
            color = colorScheme.onBackground,
            lineHeight = 28.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = stringResource(id = R.string.ReportsScreen_NoReports_Description),
            style = MyFontStyle.medium(),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun TransactionsList(
    transactionsList: List<TransactionWithFund>,
    isHideData: Boolean,
    exceptTransfers: Boolean,
    expandedDays: Map<Long, Boolean>,
    onToggleDay: (Long) -> Unit,
    onTransactionClick: (TransactionWithFund) -> Unit
) {
    val grouped = remember(transactionsList) {
        transactionsList
            .groupBy { it.transaction.date.startOfDay() }
            .toSortedMap(reverseOrder())
    }
    val itemsList = grouped.entries.toList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsList.forEachIndexed { index, (dayStart, dayTransactions) ->
            val isExpanded = expandedDays[dayStart] ?: false
            val total = dayTransactions
                .filter { !exceptTransfers || !it.transaction.isTransfer }
                .sumOf { it.transaction.amount }

            ReportCards(
                isHideData = isHideData,
                isExpended = isExpanded,
                date = dayStart.toDayHeader(),
                dayExpensesTotal = total,
                onHeaderClick = { onToggleDay(dayStart) },
                transactions = dayTransactions,
                onTransactionClick = onTransactionClick
            )
            if (index != itemsList.lastIndex) {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun ReportCards(
    isHideData: Boolean,
    isExpended: Boolean,
    date: String,
    dayExpensesTotal: Double,
    onHeaderClick: () -> Unit,
    transactions: List<TransactionWithFund>,
    onTransactionClick: (TransactionWithFund) -> Unit
) {
    val rotationState by animateFloatAsState(targetValue = if (isExpended) 270f else 90f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MyRoundedCornerShape.medium)
            .heightIn(min = 50.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onHeaderClick()
            }
            .border(
                color = colorScheme.primary.copy(alpha = 0.7f),
                shape = MyRoundedCornerShape.medium,
                width = (0.5).dp
            )
            .background(colorScheme.surface)
            .padding(horizontal = 10.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = date,
                textAlign = TextAlign.Start,
                color = colorScheme.onSurface,
                style = MyFontStyle.medium(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.weight(1f))

            HideTextData(
                isHideData = isHideData,
                text = dayExpensesTotal.toString(),
                color = colorScheme.onBackground,
                style = MyFontStyle.large(),
                align = Alignment.CenterEnd,
            )
            AppCurrency(textColor = colorScheme.onBackground)
            Spacer(Modifier.width(5.dp))

            MyIcons.Arrow(
                modifier = Modifier.offset(y = (-2.5).dp),
                autoMirroredState = false,
                angle = rotationState,
                size = 20.dp,
                color = colorScheme.onBackground,
            )
        }
        if (isExpended) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = (0.5).dp,
                    color = colorScheme.primary.copy(alpha = 0.7f)
                )

                transactions.forEachIndexed { index, transaction ->
                    TransactionItemProv(
                        isHideData = isHideData,
                        transaction = transaction,
                        onTransactionClick = { onTransactionClick(transaction) }
                    )
                    if (index != transactions.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            thickness = 0.5.dp,
                            color = colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItemProv(
    isHideData: Boolean = false,
    transaction: TransactionWithFund,
    onTransactionClick: () -> Unit = {}
) {
    if (!transaction.transaction.isTransfer) TransactionItem(
        isHideData,
        transaction,
        onTransactionClick
    )
    else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .clip(MyRoundedCornerShape.medium)
                    .border(
                        shape = MyRoundedCornerShape.medium,
                        width = 1.dp,
                        color = colorScheme.surfaceContainerHigh
                    )
                    .background(colorScheme.surfaceContainerHigh.copy(alpha = 0.09f)),
                contentAlignment = Alignment.Center
            ) {
                TransactionItem(isHideData, transaction, onTransactionClick)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 14.dp)
                    .clip(MyRoundedCornerShape.medium)
                    .background(colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .clip(MyRoundedCornerShape.medium)
                        .border(
                            shape = MyRoundedCornerShape.medium,
                            width = 1.dp,
                            color = colorScheme.surfaceContainerHigh
                        )
                        .background(colorScheme.surfaceContainerHigh.copy(alpha = 0.2f))
                        .padding(7.dp)
                        .padding(horizontal = 3.dp),
                    text = stringResource(R.string.ReportsScreen_TransferTransaction),
                    textAlign = TextAlign.Center,
                    color = colorScheme.onBackground,
                    style = MyFontStyle.small(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(
    isHideData: Boolean,
    transaction: TransactionWithFund,
    onTransactionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onTransactionClick()
            }
            .padding(10.dp)
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 5.dp)
        ) {
            HideTextData(
                isHideData = isHideData,
                text = transaction.transaction.description,
                color = colorScheme.onBackground,
                style = MyFontStyle.medium(),
                align = Alignment.CenterStart,
                maxLines = 2
            )
            Spacer(Modifier.height(5.dp))
            HideTextData(
                isHideData = isHideData,
                text = transaction.fund.name,
                color = colorScheme.onSurface,
                style = MyFontStyle.xSmall(),
                align = Alignment.CenterStart
            )
        }
        HideTextData(
            modifier = Modifier.weight(1f),
            isHideData = isHideData,
            text = doubleFormat(transaction.transaction.amount),
            color = colorScheme.onBackground,
            style = MyFontStyle.xLarge(),
            align = Alignment.CenterEnd
        )
        AppCurrency(textColor = colorScheme.onBackground)
    }
}

/** --------------------[ Helpers ]-------------------- **/
fun formatDate(timestamp: Long?): String {
    return if (timestamp == null) {
        ""
    } else {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val sdf = SimpleDateFormat("MMMM، yyyy", Locale.forLanguageTag("ar"))
        sdf.format(cal.time)
    }
}

fun Long.toDayHeader(): String {
    val cal = Calendar.getInstance().apply { timeInMillis = this@toDayHeader }
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.forLanguageTag("ar"))
    val year = cal.get(Calendar.YEAR)
    val weekday =
        cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.forLanguageTag("ar"))
    return "$weekday $day $month $year"
}

fun Long.startOfDay(): Long {
    val cal = Calendar.getInstance().apply { timeInMillis = this@startOfDay }
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}