package com.axoncodelabs.cashbox.ui.screens.funds.components.fundoptions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.components.MyTextField
import com.axoncodelabs.cashbox.ui.screens.funds.FundsEvent
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.theme.hideDataMask

@Composable
fun FundOptionsSheet(
    hideBlurState: Dp,
    isHideData: Boolean?,
    fund: FundEntity,
    viewModel: FundOptionsVM = hiltViewModel(),
    onClose: () -> Unit,
) {
    LaunchedEffect(key1 = fund.id) {
        viewModel.initData(fund)
    }
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

    if (viewModel.showDeleteFundTransPopup) {
        Dialog(
            onDismissRequest = { viewModel.showDeleteFundTransPopup = false }
        ) {
            DeleteFundTransactionsPopup(
                onDelete = {
                    viewModel.onEvent(FundOptionsEvent.OnDeleteClick)
                    viewModel.showDeleteFundTransPopup = false
                },
                onCancel = { viewModel.showDeleteFundTransPopup = false }
            )
        }
    }

    FundOptionsSheetRoot(
        hideBlurState = hideBlurState,
        isHideData = isHideData,
        name = viewModel.name,
        isEditMode = viewModel.isEditMode,
        onEditFundClick = { viewModel.onEvent(FundOptionsEvent.OnEditFundClick) },
        onNameChange = { viewModel.onEvent(FundOptionsEvent.OnNameChange(it)) },
        isNameEmpty = viewModel.isNameEmpty,
        onSaveClick = { viewModel.onEvent(FundOptionsEvent.OnSaveClick) },
        onDeleteFundTransactionClick = { viewModel.showDeleteFundTransPopup = true }
    )

}

@Composable
private fun FundOptionsSheetRoot(
    hideBlurState: Dp,
    isHideData: Boolean?,
    name: String,
    isEditMode: Boolean,
    onEditFundClick: () -> Unit,
    onNameChange: (String) -> Unit,
    isNameEmpty: Boolean,
    onSaveClick: () -> Unit,
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
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(end = 10.dp)
                        .wrapContentSize()
                        .blur(hideBlurState)
                ) {
                    MyTextField(
                        value = hideDataMask(isHideData, text = (name)),
                        onValueChange = onNameChange,
                        keyboardType = KeyboardType.Text,
                        singleLine = true,
                        isError = isNameEmpty,
                        errorMsg = stringResource(R.string.Sheet_FundNameError)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(MyRoundedCornerShape.medium)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                        .padding(10.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSaveClick() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .wrapContentSize()
                        .blur(hideBlurState)
                ) {
                    Text(
                        text = hideDataMask(isHideData, text = (name)),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MyFontStyle.medium(),
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(MyRoundedCornerShape.medium)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                        .padding(10.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onEditFundClick() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .clip(MyRoundedCornerShape.large),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = stringResource(R.string.Sheet_DeleteFundTransactions),
            color = MaterialTheme.colorScheme.error,
            style = MyFontStyle.medium(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDeleteFundTransactionClick() }
        )
    }
}