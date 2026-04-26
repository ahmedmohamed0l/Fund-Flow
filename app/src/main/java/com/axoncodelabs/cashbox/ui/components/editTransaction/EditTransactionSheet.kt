package com.axoncodelabs.cashbox.ui.components.editTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.ui.components.DateSelection
import com.axoncodelabs.cashbox.ui.components.MyNumField
import com.axoncodelabs.cashbox.ui.components.MyTextField
import com.axoncodelabs.cashbox.ui.components.fundselection.FundSelectionBttn
import com.axoncodelabs.cashbox.ui.screens.expenses.ExpensesEvent
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun EditTransactionSheet(
    isHideData: Boolean,
    transaction: TransactionWithFund,
    viewModel: EditTransactionVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = transaction.transaction.id) {
        viewModel.initTransaction(transaction)
    }
    LaunchedEffect(key1 = true) {
        viewModel.expensesEvent.collect { event ->
            when (event) {
                ExpensesEvent.CloseSheet -> {
                    viewModel.updateSaveBttnState()
                    onClose()
                }

                else -> Unit
            }
        }
    }

    if (viewModel.showDeleteTransactionPopup) {
        Dialog(onDismissRequest = { viewModel.showDeleteTransactionPopup = false }) {
            DeleteTransactionPopup(
                onDelete = {
                    viewModel.onEvent(EditTransactionEvent.OnDeleteClick)
                    viewModel.showDeleteTransactionPopup = false
                },
                onCancel = { viewModel.showDeleteTransactionPopup = false }
            )
        }
    }

    EditTransactionSheetRoot(
        isHideData = isHideData,
        fund = viewModel.fund,
        onFundChanged = { viewModel.onEvent(EditTransactionEvent.OnFundChanged(it)) },
        amount = viewModel.amount,
        onAmountChange = { viewModel.onEvent(EditTransactionEvent.OnAmountChange(it)) },
        isAmountEmpty = viewModel.isAmountEmpty,
        description = viewModel.description,
        onDescriptionChange = { viewModel.onEvent(EditTransactionEvent.OnDescriptionChange(it)) },
        isDescriptionEmpty = viewModel.isDescriptionEmpty,
        selectedDate = viewModel.selectedDate,
        onDateChange = { viewModel.onEvent(EditTransactionEvent.OnDateChange(it)) },
        isSaveEnabled = viewModel.isSaveBttnEnabled,
        onSaveClick = { viewModel.onEvent(EditTransactionEvent.OnSaveClick) },
        onDeleteClick = { viewModel.showDeleteTransactionPopup = true },
        onCancelClick = { onClose() },
    )
}

/**.....( Screen Layout ).....**/
@Composable
private fun EditTransactionSheetRoot(
    isHideData: Boolean,
    fund: FundEntity?,
    onFundChanged: (FundEntity) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    isAmountEmpty: Boolean,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isDescriptionEmpty: Boolean,
    selectedDate: Long,
    onDateChange: (Long) -> Unit,
    isSaveEnabled: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp), horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
                onFundSelected = onFundChanged,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                maxLines = 3,
                isError = isDescriptionEmpty,
                errorMsg = stringResource(R.string.Sheet_AddFundDescriptionError)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
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
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(3f)
                    .clickable(
                        enabled = isSaveEnabled,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSaveClick() },
                text = stringResource(R.string.Sheet_EditTransaction_Bttn),
                textAlign = TextAlign.Start,
                color = if (isSaveEnabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                style = MyFontStyle.medium()
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDeleteClick() },
                text = stringResource(R.string.Delete_Bttn),
                color = MaterialTheme.colorScheme.error,
                style = MyFontStyle.medium()
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCancelClick() },
                text = stringResource(R.string.Cancel_Bttn),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }
    }
}