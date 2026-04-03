package com.axoncodelabs.cashbox.ui.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Enum
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.topAppBar.TopBarState
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import javax.annotation.meta.When

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel(),
    onTopBarChange: (TopBarState) -> Unit
) {
    //.....( State & ViewModel Setup ).....
    val state = viewModel.state.collectAsState().value
    val sheet = state.currentSheet
    val popup = state.popupState
    val isHideData = state.isHideData
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    //.....( TopAppBar Data ).....
    LaunchedEffect(Unit) {
        onTopBarChange(
            TopBarState(titleRes = R.string.ReportsScreen_Identifier)
        )
    }

    //.....( Sheets & Popups Handling ).....
    @Composable
    fun sheetsHandle() {
        when (sheet) {
            is ReportsSheets.EditTransaction -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ReportsEvent.CloseSheet) },
                    containerColor = colorScheme.background,
                    sheetState = sheetState
                ) {
                    /*EditTransactionSheet(
                        isHideData = isHideData,
                        transaction = sheet.expense,
                        onClose = { viewModel.onEvent(ExpensesEvent.CloseSheet) }
                    )*/
                }
            }

            ReportsSheets.FundSelection -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(ReportsEvent.CloseSheet) },
                    containerColor = colorScheme.background,
                    sheetState = sheetState
                ) {
                    /*FundSelectionSheet(
                        isHideData = isHideData,
                        onSelect = {
                            onFundSelected(it)
                            viewModel.showFunds = false
                        },
                        fromFundId = fromFundId
                    )*/
                }
            }

            ReportsSheets.None -> Unit
        }
    }
    sheetsHandle()

    @Composable
    fun popupsHandle() {
        when (popup) {
            ReportsPopups.DateSelection -> {
                //TODO ToDo: Create Date Selection Popup.
            }

            ReportsPopups.None -> Unit
        }
    }
    popupsHandle()

    //.....( Screen Layout ).....
    ReportsScreenRoot()
}

/**.....( Screen Layout ).....**/
@Composable
//TODO ToDo: Make it Private (Fun).
fun ReportsScreenRoot() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        DataSelectors(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(MyRoundedCornerShape.medium)
                .border(
                    1.dp,
                    colorScheme.primary.copy(alpha = 0.5f),
                    MyRoundedCornerShape.medium
                )
                .background(colorScheme.surface)
                .padding(10.dp),
            onFundSelectorClick = {},
            onDateSelectorClick = {}
        )
        Spacer(Modifier.height(20.dp))



        Spacer(Modifier.height(20.dp))


        Spacer(Modifier.height(20.dp))


    }
}

/** --------------------[ Components ]-------------------- **/
/**.....( Data Selectors ).....**/
@Composable
private fun DataSelectors(
    modifier: Modifier = Modifier,
    onFundSelectorClick: () -> Unit,
    onDateSelectorClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FundSelector(onFundSelectorClick = onFundSelectorClick)
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clip(MyRoundedCornerShape.medium),
            thickness = 1.dp,
            color = colorScheme.primary.copy(alpha = 0.5f)
        )
        DateSelector(onDateSelectorClick = onDateSelectorClick)
    }
}

@Composable
private fun FundSelector(onFundSelectorClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onFundSelectorClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.CreditCard(color = colorScheme.onBackground, size = 30.dp)
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 4.dp),
                text = stringResource(id = R.string.ReportsScreen_FundSelection),
                color = colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = "الكل", //TODO ToDo: Get Fund Name State.
                color = colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
            MyIcons.Arrow(
                autoMirroredState = false,
                angle = 90f,
                size = 25.dp,
                color = colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun DateSelector(onDateSelectorClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onDateSelectorClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.Time_Zone(color = colorScheme.onBackground, size = 30.dp)
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 4.dp),
                text = stringResource(id = R.string.ReportsScreen_FundSelection),
                color = colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = "الكل", //TODO ToDo: Get Date State.
                color = colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
            MyIcons.Arrow(
                autoMirroredState = false,
                angle = 90f,
                size = 25.dp,
                color = colorScheme.onBackground,
            )
        }
    }
}

/**.....( Report Type Selector ).....**/
@Composable
//TODO ToDo: Make it Private (Fun).
fun ReportTypeSelector(
    selectedType: ReportType,
    onTypeSelected: (ReportType) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MyRoundedCornerShape.medium)
            .background(colorScheme.primary)
            .padding(5.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}