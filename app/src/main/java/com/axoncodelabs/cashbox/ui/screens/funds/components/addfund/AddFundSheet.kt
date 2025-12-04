package com.axoncodelabs.cashbox.ui.screens.funds.components.addfund

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
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.MyBotton
import com.axoncodelabs.cashbox.ui.components.MyLabel
import com.axoncodelabs.cashbox.ui.components.MyTextField
import com.axoncodelabs.cashbox.ui.screens.UiEvent

@Composable
fun AddFundSheet(
    viewModel: AddFundVM = hiltViewModel(),
    onClose: () -> Unit,
    /*Disable ShowSnackbar
    snackbarHostState: SnackbarHostState,*/
) {
    /*Disable ShowSnackbar
    val context = LocalContext.current*/
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                /*Disable ShowSnackbar
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.message),
                        actionLabel = event.action
                    )
                    Log.d("AddFundVM", "SHOW SNACKBAR TRIGGERED!!! name = ${context.getString(event.message)}")
                }
                */
                is UiEvent.CloseSheet -> onClose()
                else -> Unit
            }
        }
    }

    AddFundSheetRoot(
        name = viewModel.name,
        amount = viewModel.amount,
        description = viewModel.description,
        onNameChange = { viewModel.onEvent(AddFundEvent.OnNameChange(it)) },
        onAmountChange = { viewModel.onEvent(AddFundEvent.OnAmountChange(it)) },
        onDescriptionChange = { viewModel.onEvent(AddFundEvent.OnDescriptionChange(it)) },
        onSaveClick = { viewModel.onEvent(AddFundEvent.OnSaveClick) },
        isNameEmpty = viewModel.isNameEmpty,
        isAmountNegative = viewModel.isAmountNegative
    )
}

@Composable
private fun AddFundSheetRoot(
    name: String,
    amount: String,
    description: String,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isNameEmpty: Boolean,
    isAmountNegative: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp), horizontalAlignment = Alignment.Start
    ) {
        MyLabel(stringResource(R.string.Sheet_FundName))
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(
            value = name,
            onValueChange = onNameChange,
            hintText = stringResource(R.string.Sheet_FundName),
            keyboardType = KeyboardType.Text,
            singleLine = true,
            isError = isNameEmpty,
            errorMsg = stringResource(R.string.Sheet_FundNameError)
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyLabel(stringResource(R.string.Sheet_Amount_Lapel))
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(
            value = amount,
            onValueChange = onAmountChange,
            hintText = stringResource(R.string.Sheet_Amount_Hint),
            keyboardType = KeyboardType.Number,
            singleLine = true,
            isError = isAmountNegative,
            errorMsg = stringResource(R.string.Sheet_FundAmountError)
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyLabel(stringResource(R.string.Sheet_DescriptionLapel))
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(
            value = description,
            onValueChange = onDescriptionChange,
            hintText = stringResource(R.string.Sheet_Description_Hint),
            keyboardType = KeyboardType.Text,
            singleLine = false,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyBotton(
            text = stringResource(R.string.Sheet_Save_Bttn),
            onClick = onSaveClick
        )
    }
}