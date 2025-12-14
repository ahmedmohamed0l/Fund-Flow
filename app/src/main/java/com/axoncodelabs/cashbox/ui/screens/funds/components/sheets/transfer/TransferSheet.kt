package com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.axoncodelabs.cashbox.data.util.doubleFormat
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.MyBotton
import com.axoncodelabs.cashbox.ui.components.MyLabel
import com.axoncodelabs.cashbox.ui.components.MyNumField
import com.axoncodelabs.cashbox.ui.components.MyRoundedLabel
import com.axoncodelabs.cashbox.ui.components.MyTextField
import com.axoncodelabs.cashbox.ui.components.fundselection.FundSelectionBttn
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import com.axoncodelabs.cashbox.ui.screens.funds.components.DateSelection
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TransferSheet(
    isHideData: Boolean,
    fromFund: FundEntity,
    viewModel: TransferVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        viewModel.fundsEvent.collect { event ->
            when (event) {
                is FundsEvent.CloseSheet -> {
                    viewModel.clearSheetData()
                    onClose()
                }

                else -> Unit
            }
        }
    }
    LaunchedEffect(key1 = fromFund.id) {
        viewModel.initTransaction(fromFund)
    }

    TransferSheetRoot(
        isHideData = isHideData,
        fromFund = fromFund,
        fromName = viewModel.fromName,
        availableBalance = viewModel.availableBalance,
        isAvailableNegative = viewModel.isAvailableNegative,
        amount = viewModel.amount,
        description = viewModel.description,
        onAmountChange = { viewModel.onEvent(TransferEvent.OnAmountChange(it)) },
        onDescriptionChange = { viewModel.onEvent(TransferEvent.OnDescriptionChange(it)) },
        selectedDate = viewModel.selectedDate,
        onDateChange = { viewModel.onEvent(TransferEvent.OnDateChange(it)) },
        onSaveClick = { viewModel.onEvent(TransferEvent.OnSaveClick) },
        isAmountEmpty = viewModel.isAmountEmpty,
        toFund = viewModel.toFund,
        onToFundSelected = { viewModel.onEvent(TransferEvent.OnToFundSelected(it)) },
        isNoFundSelected = viewModel.isNoFundSelected
    )
}

@Composable
private fun TransferSheetRoot(
    isHideData: Boolean,
    fromFund: FundEntity,
    fromName: String,
    availableBalance: Double,
    isAvailableNegative: Boolean,
    amount: String,
    description: String,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    selectedDate: Long,
    onDateChange: (Long) -> Unit,
    onSaveClick: () -> Unit,
    isAmountEmpty: Boolean,
    toFund: FundEntity?,
    onToFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp), horizontalAlignment = Alignment.Start
    ) {
        MyLabel(stringResource(R.string.Sheet_Fund_From_Name))
        Spacer(modifier = Modifier.height(10.dp))
        MyRoundedLabel(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            lapel = fromName,
            textColor = MaterialTheme.colorScheme.onBackground
        )


        Spacer(modifier = Modifier.height(20.dp))

        MyLabel(stringResource(R.string.Sheet_Fund_To_Name))
        Spacer(modifier = Modifier.height(10.dp))
        FundSelectionBttn(
            isHideData = isHideData,
            fund = toFund,
            fromFundId = fromFund.id,
            onFundSelected = onToFundSelected,
            isUnSelected = isNoFundSelected,
            unSelectedErrorMsg = stringResource(R.string.Sheet_NoFundSelected)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyLabel(stringResource(R.string.Sheet_Amount_Lapel))
            HideTextData(
                isHideData = isHideData,
                text = (stringResource(R.string.Sheet_FundFromBalance) + " " + doubleFormat(
                    availableBalance
                )),
                color = if (isAvailableNegative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.inversePrimary,
                style = MyFontStyle.medium()
            )
        }
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
            date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(Date(selectedDate)),
            onDateIncrease = {
                val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
                cal.add(Calendar.DAY_OF_MONTH, 1)
                onDateChange(cal.timeInMillis)
            },
        )
        Spacer(modifier = Modifier.height(15.dp))

        MyBotton(
            text = stringResource(R.string.Sheet_AddTransaction_Bttn),
            onClick = onSaveClick
        )
    }
}