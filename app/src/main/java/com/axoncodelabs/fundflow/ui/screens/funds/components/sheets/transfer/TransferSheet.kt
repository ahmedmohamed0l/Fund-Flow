package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.ui.components.DateSelection
import com.axoncodelabs.fundflow.ui.components.MainBttn
import com.axoncodelabs.fundflow.ui.components.sheets.AvailableBalanceSection
import com.axoncodelabs.fundflow.ui.components.sheets.FundNameSection
import com.axoncodelabs.fundflow.ui.components.sheets.LabelAmountSection
import com.axoncodelabs.fundflow.ui.components.sheets.LabelDescriptionSection
import com.axoncodelabs.fundflow.ui.components.sheets.SheetFieldLabel
import com.axoncodelabs.fundflow.ui.components.sheets.fundSelection.FundSelectionBttn
import java.util.Calendar

@Composable
fun TransferSheet(
    isHideData: Boolean,
    fromFund: FundEntity,
    viewModel: TransferVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = fromFund.id) {
        viewModel.initTransaction(fromFund)
    }

    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }

    TransferSheetRoot(
        isHideData = isHideData,

        fromFund = fromFund,

        toFund = viewModel.toFund,
        onToFundSelected = { viewModel.onEvent(TransferEvent.OnToFundSelected(it)) },
        isNoFundSelected = viewModel.isNoFundSelected,

        availableBalance = viewModel.availableBalance,
        isAvailableNegative = viewModel.isAvailableNegative,

        amount = viewModel.amount,
        onAmountChange = { viewModel.onEvent(TransferEvent.OnAmountChange(it)) },
        isAmountEmpty = viewModel.isAmountEmpty,

        description = viewModel.description,
        onDescriptionChange = { viewModel.onEvent(TransferEvent.OnDescriptionChange(it)) },

        selectedDate = viewModel.selectedDate,
        onDateChange = { viewModel.onEvent(TransferEvent.OnDateChange(it)) },

        onSaveClick = { viewModel.onEvent(TransferEvent.OnSaveClick) }
    )
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun TransferSheetRoot(
    isHideData: Boolean,

    fromFund: FundEntity,

    toFund: FundEntity?,
    onToFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean,

    availableBalance: Double,
    isAvailableNegative: Boolean,

    amount: String,
    onAmountChange: (String) -> Unit,
    isAmountEmpty: Boolean,

    description: String,
    onDescriptionChange: (String) -> Unit,

    selectedDate: Long,
    onDateChange: (Long) -> Unit,

    onSaveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp), horizontalAlignment = Alignment.Start
    ) {
        FundNameSection(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            label = stringResource(R.string.Sheet_Fund_From_Name),
            fundName = fromFund.name,
            fundNameColor = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))

        SelectFundSection(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            fromFund = fromFund,
            toFund = toFund,
            onToFundSelected = onToFundSelected,
            isNoFundSelected = isNoFundSelected
        )
        Spacer(modifier = Modifier.height(20.dp))

        LabelAmountSection(
            modifier = Modifier.fillMaxWidth(),
            amount = amount,
            onAmountChange = onAmountChange,
            isAmountEmpty = isAmountEmpty,
            labelEndContent = {
                Spacer(modifier = Modifier.width(5.dp))
                AvailableBalanceSection(
                    isHideData = isHideData,
                    availableBalance = availableBalance,
                    isAvailableNegative = isAvailableNegative
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        LabelDescriptionSection(
            modifier = Modifier.fillMaxWidth(),
            description = description,
            onDescriptionChange = onDescriptionChange
        )
        Spacer(modifier = Modifier.height(15.dp))

        DateSelection(
            onDateDecrease = {
                val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
                cal.add(Calendar.DAY_OF_MONTH, -1)
                onDateChange(cal.timeInMillis)
            },
            date = selectedDate,
            onDateIncrease = {
                val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
                cal.add(Calendar.DAY_OF_MONTH, 1)
                onDateChange(cal.timeInMillis)
            },
        )
        Spacer(modifier = Modifier.height(15.dp))

        MainBttn(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.Sheet_AddTransaction_Bttn),
            onClick = onSaveClick
        )
    }
}

// ────────────────{ Components }────────────────
@Composable
private fun SelectFundSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fromFund: FundEntity,
    toFund: FundEntity?,
    onToFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean,
) {
    Column(modifier = modifier) {
        SheetFieldLabel(stringResource(R.string.Sheet_Fund_To_Name))
        Spacer(modifier = Modifier.height(10.dp))
        FundSelectionBttn(
            isHideData = isHideData,
            fund = toFund,
            fromFundId = fromFund.id,
            onFundSelected = onToFundSelected,
            isUnSelected = isNoFundSelected,
            unSelectedErrorMsg = stringResource(R.string.Sheet_NoFundSelected)
        )
    }
}