package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.addAmount

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
import com.axoncodelabs.fundflow.ui.components.DateSelection
import com.axoncodelabs.fundflow.ui.components.MainBttn
import com.axoncodelabs.fundflow.ui.components.sheets.FundNameSection
import com.axoncodelabs.fundflow.ui.components.sheets.LabelAmountSection
import com.axoncodelabs.fundflow.ui.components.sheets.LabelDescriptionSection
import java.util.Calendar

@Composable
fun AddAmountSheet(
    isHideData: Boolean,
    fund: FundEntity,
    viewModel: AddAmountVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = fund.id) {
        viewModel.initData(fund)
    }

    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }


    AddAmountSheetRoot(
        isHideData = isHideData,

        fundName = viewModel.fund?.name.orEmpty(),

        amount = viewModel.amount,
        onAmountChange = { viewModel.onEvent(AddAmountEvent.OnAmountChange(it)) },
        isAmountEmpty = viewModel.isAmountEmpty,

        description = viewModel.description,
        onDescriptionChange = { viewModel.onEvent(AddAmountEvent.OnDescriptionChange(it)) },

        selectedDate = viewModel.selectedDate,
        onDateChange = { viewModel.onEvent(AddAmountEvent.OnDateChange(it)) },

        onSaveClick = { viewModel.onEvent(AddAmountEvent.OnSaveClick) }
    )
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun AddAmountSheetRoot(
    isHideData: Boolean,

    fundName: String,

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
            label = stringResource(R.string.Sheet_FundName),
            fundName = fundName
        )
        Spacer(modifier = Modifier.height(20.dp))

        LabelAmountSection(
            modifier = Modifier.fillMaxWidth(),
            amount = amount,
            onAmountChange = onAmountChange,
            isAmountEmpty = isAmountEmpty
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