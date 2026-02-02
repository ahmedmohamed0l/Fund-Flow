package com.axoncodelabs.cashbox.ui.screens.expenses.components.sheets.addExpenseSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.util.doubleFormat
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.MyButton
import com.axoncodelabs.cashbox.ui.components.MyNumField
import com.axoncodelabs.cashbox.ui.components.MyTextField
import com.axoncodelabs.cashbox.ui.components.fundselection.FundSelectionBttn
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons

@Composable
fun AddExpenseSheet(
    isHideData: Boolean,
    selectedDate: Long,
    viewModel: AddExpenseVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        viewModel.fundsEvent.collect { event ->
            when (event) {
                FundsEvent.CloseSheet -> {
                    viewModel.clearSheetData()
                    onClose()
                }

                else -> Unit
            }
        }
    }
    LaunchedEffect(key1 = selectedDate) {
        viewModel.initTransaction(selectedDate)
    }

    AddExpenseSheetRoot(
        isHideData = isHideData,
        availableBalance = viewModel.availableBalance,
        isAvailableNegative = viewModel.isAvailableNegative,
        amount = viewModel.amount,
        description = viewModel.description,
        onAmountChange = { viewModel.onEvent(AddExpenseEvent.OnAmountChange(it)) },
        onDescriptionChange = { viewModel.onEvent(AddExpenseEvent.OnDescriptionChange(it)) },
        onSaveClick = { viewModel.onEvent(AddExpenseEvent.OnSaveClick) },
        isAmountEmpty = viewModel.isAmountEmpty,
        onFundSelected = { viewModel.onEvent(AddExpenseEvent.OnFundSelected(it)) },
        isNoFundSelected = viewModel.isNoFundSelected
    )

}

@Composable
private fun AddExpenseSheetRoot(
    isHideData: Boolean,
    availableBalance: Double,
    isAvailableNegative: Boolean,
    amount: String,
    description: String,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isAmountEmpty: Boolean,
    onFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp), horizontalAlignment = Alignment.Start
    ) {
        FundSelectionBttn(
            isHideData = isHideData,
            fund = fund,
            //TODO CHECKPOINT: continue from here1
            onFundSelected = onFundSelected,
            isUnSelected = isNoFundSelected,
            unSelectedErrorMsg = stringResource(R.string.Sheet_NoFundSelected)
        )
        Spacer(modifier = Modifier.height(5.dp))

        HideTextData(
            modifier = Modifier.align(Alignment.End),
            isHideData = isHideData,
            text = (stringResource(R.string.Sheet_FundFromBalance) + " " + doubleFormat(
                availableBalance
            )),
            color = if (isAvailableNegative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.inversePrimary,
            style = MyFontStyle.small()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyIcons.Money(color = MaterialTheme.colorScheme.onBackground, size = 30.dp)
            Spacer(modifier = Modifier.width(10.dp))
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
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyIcons.Description(
                color = MaterialTheme.colorScheme.onBackground,
                size = 30.dp,
                modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            MyTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = onDescriptionChange,
                hintText = stringResource(R.string.Sheet_Description_Hint),
                keyboardType = KeyboardType.Text,
                singleLine = false,
                maxLines = 3
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        MyButton(
            text = stringResource(R.string.Sheet_AddTransaction_Bttn),
            onClick = onSaveClick
        )
    }
}

/*
/**--------------------[ Preview ]--------------------**/
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        val darkMode = false
        CashBoxTheme(darkTheme = darkMode) {
            AddExpenseSheetRoot(
                isHideData = false,
                fund = FundEntity(
                    id = 0,
                    name = "",
                    balance = 0.0
                ),
                availableBalance = 0.0,
                isAvailableNegative = false,
                amount = "",
                description = "",
                onAmountChange = {},
                onDescriptionChange = {},
                onSaveClick = {},
                isAmountEmpty = false,
                onFundSelected = {},
                isNoFundSelected = false
            )
        }
    }
}*/