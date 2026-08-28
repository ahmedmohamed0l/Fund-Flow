package com.axoncodelabs.fundflow.ui.components.sheets.editTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.relation.TransactionWithFund
import com.axoncodelabs.fundflow.ui.components.DateSelection
import com.axoncodelabs.fundflow.ui.components.hideDataMask
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.components.sheets.IconAmountSection
import com.axoncodelabs.fundflow.ui.components.sheets.IconDescriptionSection
import com.axoncodelabs.fundflow.ui.components.sheets.IconFundSelectionSection
import com.axoncodelabs.fundflow.ui.components.sheets.SheetResult
import com.axoncodelabs.fundflow.ui.components.sheets.popups.DeletePopup
import com.axoncodelabs.fundflow.ui.components.sheets.popups.PopupResult
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import java.util.Calendar

@Composable
fun EditTransactionSheet(
    isHideData: Boolean,
    transaction: TransactionWithFund,
    viewModel: EditTransactionVM = hiltViewModel(),
    onClose: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val showDeletePopup = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = transaction.transaction.id) {
        viewModel.initTransaction(transaction)
    }

    // Snackbar Messages
    val transactionUpdated = stringResource(R.string.Snackbar_Transaction_Updated)
    val transactionDeleted = stringResource(R.string.Snackbar_Transaction_Deleted)

    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.endSheetEvent.collect { result ->
            when (result) {
                SheetResult.Updated -> {
                    onShowSnackbar(transactionUpdated)
                    onClose()
                }

                SheetResult.Deleted -> {
                    onShowSnackbar(transactionDeleted)
                    onClose()
                }

                SheetResult.Cancelled -> onClose()

                else -> Unit
            }
        }
    }

    DeleteConfirmationDialog(
        show = showDeletePopup.value,
        onEndPopup = { result ->
            when (result) {
                PopupResult.Deleted -> {
                    viewModel.onEvent(EditTransactionEvent.OnDeleteClick)
                    showDeletePopup.value = false
                }

                PopupResult.Cancelled -> {
                    showDeletePopup.value = false
                }
            }
        }
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
    onEndPopup: (PopupResult) -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = { onEndPopup(PopupResult.Cancelled) }) {
            DeletePopup(
                title = stringResource(id = R.string.Popups_DeleteTransactionConfirm_Message),
                onEndPopup = onEndPopup
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
        IconFundSelectionSection(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            fund = fund,
            onFundSelected = onFundChanged
        )
        Spacer(modifier = Modifier.height(20.dp))

        IconAmountSection(
            modifier = Modifier.fillMaxWidth(),
            amount = hideDataMask(isHideData, text = (amount)),
            onAmountChange = onAmountChange,
            isAmountEmpty = isAmountEmpty
        )
        Spacer(modifier = Modifier.height(20.dp))

        IconDescriptionSection(
            modifier = Modifier.fillMaxWidth(),
            description = hideDataMask(isHideData, text = (description)),
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
            date = selectedDate,
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
                .noRippleClickable(isSaveEnabled) { onSaveClick() },
            text = stringResource(R.string.Sheet_EditTransaction_Bttn),
            textAlign = TextAlign.Start,
            color = if (isSaveEnabled) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.5f
            ),
            style = MyFontStyle.medium()
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { onDeleteClick() },
            text = stringResource(R.string.Delete_Bttn),
            color = MaterialTheme.colorScheme.error,
            style = MyFontStyle.medium()
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { onCancelClick() },
            text = stringResource(R.string.Cancel_Bttn),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.medium()
        )
    }
}