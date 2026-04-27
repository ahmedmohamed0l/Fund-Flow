package com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.IconAmountSection
import com.axoncodelabs.cashbox.ui.components.IconDescriptionSection
import com.axoncodelabs.cashbox.ui.components.MainBttn
import com.axoncodelabs.cashbox.ui.components.fundselection.FundSelectionBttn
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.util.doubleFormat

@Composable
fun AddExpenseSheet(
    isHideData: Boolean,
    selectedDate: Long,
    viewModel: AddExpenseVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = selectedDate) {
        viewModel.initTransaction(selectedDate)
    }

    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }

    AddExpenseSheetRoot(
        isHideData = isHideData,

        fund = viewModel.fund,
        onFundSelected = { viewModel.onEvent(AddExpenseEvent.OnFundSelected(it)) },
        isNoFundSelected = viewModel.isNoFundSelected,

        availableBalance = viewModel.availableBalance,
        isAvailableNegative = viewModel.isAvailableNegative,

        amount = viewModel.amount,
        onAmountChange = { viewModel.onEvent(AddExpenseEvent.OnAmountChange(it)) },
        isAmountEmpty = viewModel.isAmountEmpty,

        description = viewModel.description,
        onDescriptionChange = { viewModel.onEvent(AddExpenseEvent.OnDescriptionChange(it)) },
        isDescriptionEmpty = viewModel.isDescriptionEmpty,

        onSaveClick = { viewModel.onEvent(AddExpenseEvent.OnSaveClick) },
    )

}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun AddExpenseSheetRoot(
    isHideData: Boolean,

    fund: FundEntity?,
    onFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean,

    availableBalance: Double,
    isAvailableNegative: Boolean,

    amount: String,
    onAmountChange: (String) -> Unit,
    isAmountEmpty: Boolean,


    description: String,
    onDescriptionChange: (String) -> Unit,
    isDescriptionEmpty: Boolean,

    onSaveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FundSelectionSection(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            fund = fund,
            onFundSelected = onFundSelected,
            isNoFundSelected = isNoFundSelected
        )
        Spacer(modifier = Modifier.height(7.dp))

        AvailableBalanceSection(
            modifier = Modifier.align(Alignment.End),
            isHideData = isHideData,
            availableBalance = availableBalance,
            isAvailableNegative = isAvailableNegative
        )
        Spacer(modifier = Modifier.height(7.dp))

        IconAmountSection(
            modifier = Modifier.fillMaxWidth(),
            amount = amount,
            onAmountChange = onAmountChange,
            isAmountEmpty = isAmountEmpty
        )
        Spacer(modifier = Modifier.height(20.dp))

        IconDescriptionSection(
            modifier = Modifier.fillMaxWidth(),
            description = description,
            onDescriptionChange = onDescriptionChange,
            isDescriptionEmpty = isDescriptionEmpty
        )
        Spacer(modifier = Modifier.height(20.dp))

        MainBttn(
            text = stringResource(R.string.Sheet_AddTransaction_Bttn),
            onClick = onSaveClick
        )
    }
}

// ────────────────{ Components }────────────────
@Composable
private fun FundSelectionSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fund: FundEntity?,
    onFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MyIcons.TransactionWallet(
            modifier = Modifier.offset(y = (-2.5).dp),
            color = MaterialTheme.colorScheme.onBackground,
            size = 30.dp
        )
        Spacer(modifier = Modifier.width(10.dp))
        FundSelectionBttn(
            isHideData = isHideData,
            fund = fund,
            onFundSelected = onFundSelected,
            isUnSelected = isNoFundSelected,
            unSelectedErrorMsg = stringResource(R.string.Sheet_NoFundSelected)
        )
    }
}

@Composable
private fun AvailableBalanceSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    availableBalance: Double,
    isAvailableNegative: Boolean,
) {
    HideTextData(
        modifier = modifier,
        isHideData = isHideData,
        text = (stringResource(R.string.Sheet_FundFromBalance) + " (${
            doubleFormat(
                availableBalance
            )
        })"),
        color = if (isAvailableNegative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.inversePrimary,
        style = MyFontStyle.small(),
        align = Alignment.CenterEnd
    )
}