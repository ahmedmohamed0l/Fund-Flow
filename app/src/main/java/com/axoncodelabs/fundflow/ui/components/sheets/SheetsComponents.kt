package com.axoncodelabs.fundflow.ui.components.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.ui.components.HideTextData
import com.axoncodelabs.fundflow.ui.components.sheets.fundSelection.FundSelectionBttn
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons
import com.axoncodelabs.fundflow.ui.util.formatAmount

// ────────────────{ (Fields + Icon) inputs }────────────────
@Composable
fun IconFundSelectionSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fund: FundEntity?,
    onFundSelected: (FundEntity) -> Unit,
    isNoFundSelected: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
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
            onFundSelected = onFundSelected,
            isUnSelected = isNoFundSelected,
            unSelectedErrorMsg = stringResource(R.string.Sheet_NoFundSelected)
        )
    }
}

@Composable
fun IconAmountSection(
    modifier: Modifier = Modifier,
    amount: String,
    onAmountChange: (String) -> Unit,
    isAmountEmpty: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyIcons.Money(color = MaterialTheme.colorScheme.onBackground, size = 30.dp)
        Spacer(modifier = Modifier.width(10.dp))
        SheetNumField(
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
}


@Composable
fun IconDescriptionSection(
    modifier: Modifier = Modifier,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isDescriptionEmpty: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyIcons.Description(
            color = MaterialTheme.colorScheme.onBackground,
            size = 30.dp,
            modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        SheetTextField(
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
}

// ────────────────{ (Fields + Label) inputs }────────────────
@Composable
fun LabelAmountSection(
    modifier: Modifier = Modifier,
    amount: String,
    onAmountChange: (String) -> Unit,
    isAmountEmpty: Boolean,
    labelEndContent: @Composable (RowScope.() -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SheetFieldLabel(stringResource(R.string.Sheet_Amount_Lapel))
            labelEndContent?.invoke(this)
        }
        Spacer(modifier = Modifier.height(10.dp))

        SheetNumField(
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
}

@Composable
fun LabelDescriptionSection(
    modifier: Modifier = Modifier,
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
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
    }
}

// ────────────────{ Other Components }────────────────
@Composable
fun AvailableBalanceSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    availableBalance: Double,
    isAvailableNegative: Boolean,
) {
    val balance =
        if (availableBalance < 0.0) availableBalance.formatAmount() else "(${availableBalance.formatAmount()})"
    HideTextData(
        modifier = modifier,
        isHideData = isHideData,
        text = (stringResource(R.string.Sheet_FundFromBalance) + " $balance"),
        color = if (isAvailableNegative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.inversePrimary,
        style = MyFontStyle.small(),
        align = Alignment.CenterEnd
    )
}

@Composable
fun FundNameSection(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    label: String,
    fundName: String,
    fundNameColor: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        modifier = modifier
    ) {
        SheetFieldLabel(label)
        Spacer(modifier = Modifier.height(10.dp))

        SheetRoundedLabel(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            lapel = (fundName),
            textAlign = Alignment.CenterStart,
            textColor = fundNameColor
        )
    }
}