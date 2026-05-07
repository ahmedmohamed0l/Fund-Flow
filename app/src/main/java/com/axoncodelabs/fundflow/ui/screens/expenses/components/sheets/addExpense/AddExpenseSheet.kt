package com.axoncodelabs.fundflow.ui.screens.expenses.components.sheets.addExpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.axoncodelabs.fundflow.ui.components.MainBttn
import com.axoncodelabs.fundflow.ui.components.sheets.AvailableBalanceSection
import com.axoncodelabs.fundflow.ui.components.sheets.IconAmountSection
import com.axoncodelabs.fundflow.ui.components.sheets.IconDescriptionSection
import com.axoncodelabs.fundflow.ui.components.sheets.IconFundSelectionSection

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
        IconFundSelectionSection(
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
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.Sheet_AddTransaction_Bttn),
            onClick = onSaveClick
        )
    }
}