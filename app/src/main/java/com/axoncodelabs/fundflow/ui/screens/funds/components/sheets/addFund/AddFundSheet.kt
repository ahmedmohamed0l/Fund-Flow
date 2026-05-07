package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.addFund

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.ui.components.MainBttn
import com.axoncodelabs.fundflow.ui.components.sheets.SheetFieldLabel
import com.axoncodelabs.fundflow.ui.components.sheets.SheetNumField
import com.axoncodelabs.fundflow.ui.components.sheets.SheetTextField

@Composable
fun AddFundSheet(
    viewModel: AddFundVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        viewModel.initData()
    }

    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }

    AddFundSheetRoot(
        name = viewModel.name,
        onNameChange = { viewModel.onEvent(AddFundEvent.OnNameChange(it)) },
        isNameEmpty = viewModel.isNameEmpty,

        amount = viewModel.amount,
        onAmountChange = { viewModel.onEvent(AddFundEvent.OnAmountChange(it)) },

        description = viewModel.description,
        onDescriptionChange = { viewModel.onEvent(AddFundEvent.OnDescriptionChange(it)) },

        onSaveClick = { viewModel.onEvent(AddFundEvent.OnSaveClick) }
    )
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun AddFundSheetRoot(
    name: String,
    onNameChange: (String) -> Unit,
    isNameEmpty: Boolean,

    amount: String,
    onAmountChange: (String) -> Unit,

    description: String,
    onDescriptionChange: (String) -> Unit,

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
            name = name,
            onNameChange = onNameChange,
            isNameEmpty = isNameEmpty
        )
        Spacer(modifier = Modifier.height(20.dp))

        SheetFieldLabel(stringResource(R.string.Sheet_Amount_Lapel))
        Spacer(modifier = Modifier.height(10.dp))
        SheetNumField(
            modifier = Modifier.fillMaxWidth(),
            value = amount,
            onValueChange = onAmountChange,
            hintText = stringResource(R.string.Sheet_Amount_Hint),
            singleLine = true,
            wrongValueMsg = stringResource(R.string.Sheet_FundAmountError),
        )

        Spacer(modifier = Modifier.height(20.dp))

        SheetFieldLabel(stringResource(R.string.Sheet_DescriptionLapel))
        Spacer(modifier = Modifier.height(10.dp))
        SheetTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = onDescriptionChange,
            hintText = stringResource(R.string.Sheet_Description_Hint),
            keyboardType = KeyboardType.Text,
            singleLine = false,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        MainBttn(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.Sheet_Save_Bttn),
            onClick = onSaveClick
        )
    }
}

// ────────────────{ Components }────────────────
@Composable
private fun FundNameSection(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    isNameEmpty: Boolean,
) {
    Column(
        modifier = modifier
    ) {
        SheetFieldLabel(stringResource(R.string.Sheet_FundName))
        Spacer(modifier = Modifier.height(10.dp))
        SheetTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChange,
            hintText = stringResource(R.string.Sheet_FundName),
            keyboardType = KeyboardType.Text,
            singleLine = true,
            isError = isNameEmpty,
            errorMsg = stringResource(R.string.Sheet_FundNameError)
        )
    }
}