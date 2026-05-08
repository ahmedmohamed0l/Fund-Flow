package com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.fundOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.ui.components.DeletePopup
import com.axoncodelabs.fundflow.ui.components.HideTextData
import com.axoncodelabs.fundflow.ui.components.hideDataMask
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.components.sheets.SheetTextField
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyRoundedCornerShape

@Composable
fun FundOptionsSheet(
    isHideData: Boolean,
    fund: FundEntity,
    viewModel: FundOptionsVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    val showDeletePopup = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = fund.id) {
        viewModel.initData(fund)
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
            viewModel.onEvent(FundOptionsEvent.OnDeleteClick)
            showDeletePopup.value = false
        },
        onClosePop = { showDeletePopup.value = false }
    )

    FundOptionsSheetRoot(
        isHideData = isHideData,

        name = viewModel.name,
        isEditMode = viewModel.isEditMode,
        onEditFundClick = { viewModel.onEvent(FundOptionsEvent.OnEditFundClick) },
        onNameChange = { viewModel.onEvent(FundOptionsEvent.OnNameChange(it)) },
        isNameEmpty = viewModel.isNameEmpty,
        onSaveClick = { viewModel.onEvent(FundOptionsEvent.OnSaveClick) },

        isFundBalanceExcepted = viewModel.isFundBalanceExcepted,
        onExceptFundToggle = { viewModel.onEvent(FundOptionsEvent.OnExceptFundToggle) },

        onDeleteFundTransactionClick = { showDeletePopup.value = true }
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
            DeletePopup(
                title = stringResource(id = R.string.Popups_DeleteTransactionConfirm_Message),
                onDelete = onDelete,
                onCancel = onClosePop
            )
        }
    }
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun FundOptionsSheetRoot(
    isHideData: Boolean,

    name: String,
    isEditMode: Boolean,
    onEditFundClick: () -> Unit,
    onNameChange: (String) -> Unit,
    isNameEmpty: Boolean,
    onSaveClick: () -> Unit,

    isFundBalanceExcepted: Boolean,
    onExceptFundToggle: () -> Unit,

    onDeleteFundTransactionClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isEditMode) {
                EditFundName(
                    modifier = Modifier.weight(1f),
                    isHideData = isHideData,
                    name = name,
                    onNameChange = onNameChange,
                    isNameEmpty = isNameEmpty,
                    onSaveClick = onSaveClick
                )
            } else {
                ProvideFundName(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .weight(1f),
                    isHideData = isHideData,
                    name = name,
                    onEditFundClick = onEditFundClick
                )
            }
        }

        ColumnsDivider()

        ExceptFundSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            isFundBalanceExcepted = isFundBalanceExcepted,
            onExceptFundToggle = onExceptFundToggle
        )

        ColumnsDivider()

        Text(
            text = stringResource(R.string.Sheet_DeleteFundTransactions),
            color = MaterialTheme.colorScheme.error,
            style = MyFontStyle.medium(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .noRippleClickable { onDeleteFundTransactionClick() }
        )
    }
}

// ────────────────{ Components }────────────────
//──── Edit\Provide Fund Name ────
@Composable
private fun EditFundName(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    name: String,
    onNameChange: (String) -> Unit,
    isNameEmpty: Boolean,
    onSaveClick: () -> Unit,
) {
    Box(
        modifier = modifier.blur(if (isHideData) (1.5).dp else 0.dp)
    ) {
        SheetTextField(
            modifier = Modifier.fillMaxWidth(),
            value = hideDataMask(isHideData, text = (name)),
            onValueChange = onNameChange,
            keyboardType = KeyboardType.Text,
            singleLine = true,
            isError = isNameEmpty,
            errorMsg = stringResource(R.string.Sheet_FundNameError)
        )
    }
    Spacer(modifier = Modifier.width(10.dp))

    EditFundNameIcon(
        icon = Icons.Rounded.Check,
        onSaveClick = onSaveClick,
        iconColor = MaterialTheme.colorScheme.inversePrimary
    )
}

@Composable
private fun ProvideFundName(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    name: String,
    onEditFundClick: () -> Unit,
) {
    HideTextData(
        modifier = modifier,
        isHideData = isHideData,
        text = (name),
        color = MaterialTheme.colorScheme.onBackground,
        style = MyFontStyle.medium(),
        align = Alignment.CenterStart
    )

    Spacer(modifier = Modifier.width(10.dp))

    EditFundNameIcon(
        icon = Icons.Rounded.Edit,
        onSaveClick = onEditFundClick,
        iconColor = MaterialTheme.colorScheme.onBackground
    )
}

//──── Other ────
@Composable
private fun ExceptFundSection(
    modifier: Modifier = Modifier,
    isFundBalanceExcepted: Boolean,
    onExceptFundToggle: () -> Unit,
) {
    Row(
        modifier = modifier
            .noRippleClickable { onExceptFundToggle() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.Sheet_ExceptFundFromSUM),
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.medium()
        )
        Switch(
            checked = isFundBalanceExcepted,
            onCheckedChange = { onExceptFundToggle() },
            modifier = Modifier.width(70.dp),
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
            )
        )
    }
}

// ────────────────{ Tine Components }────────────────
@Composable
private fun EditFundNameIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    onSaveClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
            .padding(10.dp)
            .noRippleClickable { onSaveClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ColumnsDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .padding(vertical = 5.dp)
            .clip(MyRoundedCornerShape.large),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline
    )
}