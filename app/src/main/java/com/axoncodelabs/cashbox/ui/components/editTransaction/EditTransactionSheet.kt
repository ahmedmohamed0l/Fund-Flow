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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.relation.TransactionWithFund
import com.axoncodelabs.cashbox.ui.components.DateSelection
import com.axoncodelabs.cashbox.ui.components.IconAmountSection
import com.axoncodelabs.cashbox.ui.components.IconDescriptionSection
import com.axoncodelabs.cashbox.ui.components.fundselection.FundSelectionBttn
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
    val showDeletePopup = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = transaction.transaction.id) {
        viewModel.initTransaction(transaction)
    }

    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }

    DeleteConfirmationDialog(
        show = showDeletePopup.value,
        onDelete = {
            viewModel.onEvent(EditTransactionEvent.OnDeleteClick)
            showDeletePopup.value = false
        },
        onClosePop = { showDeletePopup.value = false }
    )

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
        onDeleteClick = { showDeletePopup.value = true },
        onCancelClick = { viewModel.onEvent(EditTransactionEvent.OnCancelClick) },
    )
}

@Composable
private fun DeleteConfirmationDialog(
    show: Boolean,
    onDelete: () -> Unit,
    onClosePop: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = onClosePop) {
            DeleteTransactionPopup(
                onDelete = onDelete,
                onCancel = onClosePop
            )
        }
    }
}

// ────────────────{ Sheet Layout }────────────────
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
            .padding(25.dp),
        horizontalAlignment = Alignment.Start
    ) {
        FundSelectionSection(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            fund = fund,
            onFundChanged = onFundChanged
        )
        Spacer(modifier = Modifier.height(20.dp))

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

        ActionsBttns(
            modifier = Modifier.fillMaxWidth(),
            isSaveEnabled = isSaveEnabled,
            onSaveClick = onSaveClick,
            onDeleteClick = onDeleteClick,
            onCancelClick = onCancelClick
        )
    }
}

// ────────────────{ Components }────────────────
@Composable
private fun FundSelectionSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fund: FundEntity?,
    onFundChanged: (FundEntity) -> Unit,
) {
    Row(
        modifier = modifier,
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
}

@Composable
private fun ActionsBttns(
    modifier: Modifier = Modifier,
    isSaveEnabled: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Row(
        modifier = modifier,
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