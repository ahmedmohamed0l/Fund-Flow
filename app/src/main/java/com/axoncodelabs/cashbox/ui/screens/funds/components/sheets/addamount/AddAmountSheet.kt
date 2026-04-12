package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addamount

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
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.components.DateSelection
import com.axoncodelabs.cashbox.ui.components.MyButton
import com.axoncodelabs.cashbox.ui.components.MyLabel
import com.axoncodelabs.cashbox.ui.components.MyNumField
import com.axoncodelabs.cashbox.ui.components.MyRoundedLabel
import com.axoncodelabs.cashbox.ui.components.MyTextField
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    LaunchedEffect(key1 = true) {
        viewModel.fundsEvent.collect { event ->
            when (event) {
                FundsEvent.CloseSheet -> {
                    onClose()
                }

                else -> Unit
            }
        }
    }

    AddAmountSheetRoot(
        isHideData = isHideData,
        name = viewModel.name,
        amount = viewModel.amount,
        description = viewModel.description,
        onAmountChange = { viewModel.onEvent(AddAmountEvent.OnAmountChange(it)) },
        onDescriptionChange = { viewModel.onEvent(AddAmountEvent.OnDescriptionChange(it)) },
        selectedDate = viewModel.selectedDate,
        onDateChange = { viewModel.onEvent(AddAmountEvent.OnDateChange(it)) },
        onSaveClick = { viewModel.onEvent(AddAmountEvent.OnSaveClick) },
        isAmountEmpty = viewModel.isAmountEmpty
    )
}

@Composable
private fun AddAmountSheetRoot(
    isHideData: Boolean,
    name: String,
    amount: String,
    description: String,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    selectedDate: Long,
    onDateChange: (Long) -> Unit,
    onSaveClick: () -> Unit,
    isAmountEmpty: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp), horizontalAlignment = Alignment.Start
    ) {
        MyLabel(stringResource(R.string.Sheet_FundName))
        Spacer(modifier = Modifier.height(10.dp))
        MyRoundedLabel(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            lapel = (name),
            textAlign = Alignment.CenterStart,
        )
        Spacer(modifier = Modifier.height(20.dp))

        MyLabel(stringResource(R.string.Sheet_Amount_Lapel))
        Spacer(modifier = Modifier.height(10.dp))
        MyNumField(
            modifier = Modifier.fillMaxWidth(),
            value = amount,
            onValueChange = onAmountChange,
            hintText = stringResource(R.string.Sheet_Amount_Hint),
            singleLine = true,
            isEmptyValue = isAmountEmpty,
            emptyValueMsg = stringResource(R.string.Sheet_AddFundAmountError),
            wrongValueMsg = stringResource(R.string.Sheet_FundAmountError),
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyLabel(stringResource(R.string.Sheet_DescriptionLapel))
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = onDescriptionChange,
            hintText = stringResource(R.string.Sheet_Description_Hint),
            keyboardType = KeyboardType.Text,
            singleLine = false,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(15.dp))

        DateSelection(
            onDateDecrease = {
                val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
                cal.add(Calendar.DAY_OF_MONTH, -1)
                onDateChange(cal.timeInMillis)
            },
            date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(selectedDate)),
            onDateIncrease = {
                val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
                cal.add(Calendar.DAY_OF_MONTH, 1)
                onDateChange(cal.timeInMillis)
            },
        )
        Spacer(modifier = Modifier.height(15.dp))

        MyButton(
            text = stringResource(R.string.Sheet_AddTransaction_Bttn), onClick = onSaveClick
        )
    }
}