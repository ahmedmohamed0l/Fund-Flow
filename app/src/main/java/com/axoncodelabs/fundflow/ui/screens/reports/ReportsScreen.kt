package com.axoncodelabs.fundflow.ui.screens.reports

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund
import com.axoncodelabs.fundflow.ui.components.AppCurrency
import com.axoncodelabs.fundflow.ui.components.EmptyPage
import com.axoncodelabs.fundflow.ui.components.HideTextData
import com.axoncodelabs.fundflow.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.components.sheets.editTransaction.EditTransactionSheet
import com.axoncodelabs.fundflow.ui.components.sheets.fundSelection.FundSelectionSheet
import com.axoncodelabs.fundflow.ui.screens.reports.componants.monthSelectionSheet.MonthSelectionSheet
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons
import com.axoncodelabs.fundflow.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.fundflow.ui.util.DateFormates
import com.axoncodelabs.fundflow.ui.util.dateFormatter
import com.axoncodelabs.fundflow.ui.util.formatAmount
import com.axoncodelabs.fundflow.ui.util.startOfDay
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel(),
    onTopBarChange: (AppTopBarState) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    //──── State & ViewModel Setup ────
    val state by viewModel.state.collectAsState()

    val sheet = state.currentSheet
    val isHideData = state.isHideData

    // Reset Selected Values
    LaunchedEffect(Unit) {
        viewModel.resetSelectedValues()
    }

    // Clear Expanded Days
    val expandedDays = remember { mutableStateMapOf<Long, Boolean>() }
    LaunchedEffect(remember { Any() }) {
        viewModel.clearExpandedDaysEvent.collect {
            expandedDays.clear()
        }
    }

    //──── AppTopBar Data ────
    SideEffect {
        onTopBarChange(
            AppTopBarState(titleResId = R.string.ReportsScreen_Identifier)
        )
    }

    //──── Sheets & Popups Handling ────
    SheetsHandler(
        sheet = sheet,
        isHideData = isHideData,

        isSelectAllFunds = state.isSelectAllFunds,
        onSelectAllFunds = { viewModel.onEvent(ReportsEvent.OnSelectAllFunds) },
        onFundSelected = { viewModel.onEvent(ReportsEvent.OnFundChanged(it)) },

        isSelectAllDates = state.isSelectAllDates,
        onSelectAllDates = { viewModel.onEvent(ReportsEvent.OnSelectAllDates) },
        isSelectCurrentMonth = state.isSelectCurrentMonth,
        onSelectCurrentMonth = { viewModel.onEvent(ReportsEvent.OnSelectCurrentMonth) },
        onDateSelected = { viewModel.onEvent(ReportsEvent.OnDateChange(it)) },

        onClose = { viewModel.onEvent(ReportsEvent.CloseSheet) },
        onShowSnackbar = onShowSnackbar
    )

    //──── Screen Layout ────
    ReportsScreenRoot(
        isHideData = isHideData,

        onFundSelectorClick = { viewModel.onEvent(ReportsEvent.SheetDisplayed(ReportsSheets.FundSelection)) },
        isSelectAllFunds = state.isSelectAllFunds,
        selectedFund = state.selectedFund?.name.orEmpty(),

        onDateSelectorClick = { viewModel.onEvent(ReportsEvent.SheetDisplayed(ReportsSheets.DateSelection)) },
        isSelectAllDates = state.isSelectAllDates,
        selectedDate = (state.selectedDate?.dateFormatter(DateFormates.MonthYear)).orEmpty(),

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
        }
    )
}

// ────────────────{ Handlers }────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetsHandler(
    sheet: ReportsSheets,
    isHideData: Boolean,

    isSelectAllFunds: Boolean,
    onSelectAllFunds: () -> Unit,
    onFundSelected: (FundEntity) -> Unit,

    isSelectAllDates: Boolean,
    onSelectAllDates: () -> Unit,
    isSelectCurrentMonth: Boolean,
    onSelectCurrentMonth: () -> Unit,
    onDateSelected: (Long) -> Unit,

    onClose: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scope = rememberCoroutineScope()
    fun dismissSheet() {
        scope.launch {
            sheetState.hide()
            onClose()
        }
    }

    if (sheet != ReportsSheets.None) {
        ModalBottomSheet(
            onDismissRequest = { dismissSheet() },
            containerColor = MaterialTheme.colorScheme.background,
            sheetState = sheetState
        ) {
            when (sheet) {
                ReportsSheets.FundSelection -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SelectValueBttn(
                            modifier = Modifier.padding(start = 20.dp),
                            isActive = isSelectAllFunds,
                            lapel = stringResource(R.string.ReportsScreen_SelectAll),
                            onClick = {
                                onSelectAllFunds()
                                dismissSheet()
                            }
                        )
                        FundSelectionSheet(
                            isHideData = isHideData,
                            onSelect = { fund ->
                                onFundSelected(fund)
                                dismissSheet()
                            },
                        )
                    }
                }

                ReportsSheets.DateSelection -> {
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
                                isActive = isSelectAllDates,
                                lapel = stringResource(R.string.ReportsScreen_SelectAll),
                                onClick = {
                                    onSelectAllDates()
                                    dismissSheet()
                                }
                            )
                            SelectValueBttn(
                                modifier = Modifier.padding(end = 20.dp),
                                isActive = isSelectCurrentMonth,
                                lapel = stringResource(R.string.ReportsScreen_SelectCurrentMonth),
                                onClick = {
                                    onSelectCurrentMonth()
                                    dismissSheet()
                                }
                            )
                        }
                        MonthSelectionSheet(
                            onSelect = { month ->
                                onDateSelected(month)
                                dismissSheet()
                            }
                        )
                    }
                }

                is ReportsSheets.EditTransaction -> {
                    EditTransactionSheet(
                        isHideData = isHideData,
                        transaction = sheet.transaction,
                        onClose = { dismissSheet() },
                        onShowSnackbar = onShowSnackbar
                    )
                }

                ReportsSheets.None -> Unit
            }
        }
    }
}

// ────────────────{ Screen Layout }────────────────
@Composable
private fun ReportsScreenRoot(
    isHideData: Boolean,

    onFundSelectorClick: () -> Unit,
    isSelectAllFunds: Boolean,
    selectedFund: String,

    onDateSelectorClick: () -> Unit,
    isSelectAllDates: Boolean,
    selectedDate: String,

    exceptTransfers: Boolean,
    onExceptClick: () -> Unit,

    selectedType: ReportType,
    onTypeSelected: (ReportType) -> Unit,

    expensesSum: Double,
    incomeSum: Double,
    expenses: List<TransactionWithFund>,
    income: List<TransactionWithFund>,

    expandedDays: MutableMap<Long, Boolean>,
    onToggleDay: (Long) -> Unit,

    onTransactionClick: (TransactionWithFund) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
                    .background(MaterialTheme.colorScheme.background)
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
                modifier = Modifier.fillMaxSize(),
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

// ────────────────{ Components }────────────────
// ────────( Data Selectors )────────
@Composable
private fun DataSelectors(
    modifier: Modifier = Modifier,
    isHideData: Boolean,

    onFundSelectorClick: () -> Unit,
    selectedFund: String,
    isSelectAllFunds: Boolean,

    onDateSelectorClick: () -> Unit,
    selectedDate: String,
    isSelectAllDates: Boolean
) {
    Column(
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                MyRoundedCornerShape.medium
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fund Selector
        SelectorBar(
            modifier = Modifier.fillMaxWidth(),
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
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )

        // Date Selector
        SelectorBar(
            modifier = Modifier.fillMaxWidth(),
            icon = painterResource(id = R.drawable.ic_search_activity),
            title = stringResource(id = R.string.ReportsScreen_ZoneSelection),
            openSheet = { onDateSelectorClick() },
            selectedValue = selectedDate,
            isSelectAll = isSelectAllDates,
            isHideData = false
        )
    }
}

//── Tini Components ──
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(2f)
                .noRippleClickable { openSheet() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = icon,
                contentDescription = "Fund Selection",
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 4.dp),
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }

        Row(
            modifier = Modifier
                .weight(3f)
                .padding(top = 4.dp)
                .noRippleClickable { openSheet() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            HideTextData(
                modifier = Modifier.weight(1f),
                isHideData = isHideData,
                text = if (isSelectAll) stringResource(R.string.ReportsScreen_AllSelected) else selectedValue,
                color = MaterialTheme.colorScheme.primary,
                style = MyFontStyle.mediumBold(),
                align = Alignment.CenterEnd
            )
            MyIcons.Arrow(
                modifier = Modifier.offset(y = (-2.5).dp),
                autoMirroredState = true,
                size = 20.dp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun SelectValueBttn(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    lapel: String,
    onClick: () -> Unit
) {
    val isEnable = !isActive
    Box(
        modifier = modifier
            .noRippleClickable(isEnable) { onClick() }
            .clip(CircleShape)
            .border(
                color = if (isEnable) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                shape = CircleShape,
                width = (0.5).dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(7.dp),
            text = lapel,
            color = if (isEnable) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline,
            style = MyFontStyle.xSmall()
        )
    }
}

// ────────( Transfers Exception )────────
@Composable
private fun TransfersException(
    modifier: Modifier = Modifier,
    exceptTransfers: Boolean,
    onExceptClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .noRippleClickable { onExceptClick() }
            .border(
                shape = MyRoundedCornerShape.medium,
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
            .background(MaterialTheme.colorScheme.surface)
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
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.ReportsScreen_ExceptTransfer),
                color = MaterialTheme.colorScheme.onBackground,
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
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                uncheckedBorderColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.8f),
                uncheckedThumbColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                checkedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        )
    }
}

// ────────( Report Type Selector )────────
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
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp),
    ) {
        // Expenses
        ReportTab(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
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
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
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

//── Tini Components ──
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
        targetValue = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(durationMillis = 300)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary,
        animationSpec = tween(durationMillis = 300)
    )
    Column(
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .background(tabColor)
            .noRippleClickable { onClick() },
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
                text = value.formatAmount(),
                color = textColor,
                style = MyFontStyle.large()
            )
            AppCurrency(textColor = textColor)
        }
    }
}

// ────────( Report List )────────
@Composable
private fun ReportsPageState(
    modifier: Modifier = Modifier,
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
                TransactionsList(
                    modifier = modifier,
                    transactionsList = expenses,
                    isHideData = isHideData,
                    exceptTransfers = exceptTransfers,
                    onTransactionClick = onTransactionClick,
                    expandedDays = expandedDays,
                    onToggleDay = onToggleDay
                )
            }

            ReportType.Income -> {
                TransactionsList(
                    modifier = modifier,
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

//── Tini Components ──
@Composable
private fun TransactionsList(
    modifier: Modifier = Modifier,
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

    if (transactionsList.isEmpty())
        EmptyPage(
            modifier = modifier,
            ifImageFirst = true,
            topText = stringResource(id = R.string.ReportsScreen_NoReports_Title),
            image = painterResource(id = R.drawable.img_reports_noreports),
            imageScale = 1.1f,
            imageSize = 210.dp,
            bottomText = stringResource(id = R.string.ReportsScreen_NoReports_Description),
            bottomSpace = 80.dp
        )
    else {
        Column(
            modifier = modifier.padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsList.forEachIndexed { index, (dayStart, dayTransactions) ->
                val isExpanded = expandedDays[dayStart] ?: false
                val total = dayTransactions
                    .filter { !exceptTransfers || !it.transaction.isTransfer }
                    .sumOf { it.transaction.amount }

                DayReportCard(
                    modifier = Modifier.fillMaxWidth(),
                    isHideData = isHideData,
                    isExpended = isExpanded,
                    date = dayStart.dateFormatter(DateFormates.FullDate),
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
}

// ────────( Day Report Card )────────
@Composable
private fun DayReportCard(
    modifier: Modifier = Modifier,
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
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .heightIn(min = 50.dp)
            .noRippleClickable { onHeaderClick() }
            .border(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                shape = MyRoundedCornerShape.medium,
                width = (0.5).dp
            )
            .background(MaterialTheme.colorScheme.surface)
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
        DayReportCardHeader(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            date = date,
            dayExpensesTotal = dayExpensesTotal,
            arrowRotationState = rotationState
        )

        if (isExpended) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = (0.5).dp,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                )

                transactions.forEachIndexed { index, transaction ->
                    TransactionItemProv(
                        modifier = Modifier.fillMaxWidth(),
                        isHideData = isHideData,
                        transaction = transaction,
                        onTransactionClick = { onTransactionClick(transaction) }
                    )
                    if (index != transactions.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

//── Tini Components ──
@Composable
private fun DayReportCardHeader(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    date: String,
    dayExpensesTotal: Double,
    arrowRotationState: Float,
) {
    Row(
        modifier = modifier.padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = date,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MyFontStyle.medium(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.weight(1f))

        HideTextData(
            isHideData = isHideData,
            text = dayExpensesTotal.formatAmount(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.large(),
            align = Alignment.CenterEnd,
        )
        AppCurrency(textColor = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.width(5.dp))

        MyIcons.Arrow(
            modifier = Modifier.offset(y = (-2.5).dp),
            autoMirroredState = false,
            angle = arrowRotationState,
            size = 20.dp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

/** > **── Transaction Item Provider [[Normal/Transfer]] ──** **/
@Composable
private fun TransactionItemProv(
    modifier: Modifier = Modifier,
    isHideData: Boolean = false,
    transaction: TransactionWithFund,
    onTransactionClick: () -> Unit
) {
    if (!transaction.transaction.isTransfer)
        TransactionItem(
            modifier = modifier,
            isHideData = isHideData,
            transaction = transaction,
            onTransactionClick = onTransactionClick
        )
    else {
        TransferContainer(
            modifier = modifier,
            containerTransaction = {
                TransactionItem(
                    modifier = Modifier.fillMaxWidth(),
                    isHideData = isHideData,
                    transaction = transaction,
                    onTransactionClick = onTransactionClick
                )
            }
        )
    }
}

@Composable
private fun TransactionItem(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    transaction: TransactionWithFund,
    onTransactionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .noRippleClickable { onTransactionClick() }
            .padding(10.dp)
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 5.dp),
            horizontalAlignment = Alignment.Start
        ) {
            HideTextData(
                isHideData = isHideData,
                text = transaction.transaction.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.mediumXLight(),
                align = Alignment.CenterStart,
                maxLines = 2
            )
            Spacer(Modifier.height(5.dp))
            HideTextData(
                isHideData = isHideData,
                text = transaction.fund.name,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.xSmall(),
                align = Alignment.CenterStart
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            HideTextData(
                modifier = Modifier.weight(1f, fill = false),
                isHideData = isHideData,
                text = transaction.transaction.amount.formatAmount(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.xLarge(),
                align = Alignment.CenterEnd
            )
            AppCurrency(textColor = MaterialTheme.colorScheme.onBackground)
        }
    }
}

//── Transfer Container ──
@Composable
private fun TransferContainer(
    modifier: Modifier = Modifier,
    containerTransaction: @Composable (BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier.padding(vertical = 10.dp),
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
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.09f)),
            contentAlignment = Alignment.Center
        ) {
            containerTransaction?.invoke(this)
        }
        TransferContainerHeader(Modifier.align(Alignment.TopStart))
    }
}

@Composable
private fun TransferContainerHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 14.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .clip(MyRoundedCornerShape.medium)
                .border(
                    shape = MyRoundedCornerShape.medium,
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.2f))
                .padding(7.dp)
                .padding(horizontal = 3.dp),
            text = stringResource(R.string.ReportsScreen_TransferTransaction),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.small(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}