package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.theme.MyIcons

// ────────────────{ Icon + Fields inputs }────────────────
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

// ────────────────{ Label + Fields inputs }────────────────
@Composable
fun LabelAmountSection(
    modifier: Modifier = Modifier,
    amount: String,
    onAmountChange: (String) -> Unit,
    isAmountEmpty: Boolean,
) {
    Column(
        modifier = modifier
    ) {
        SheetFieldLabel(stringResource(R.string.Sheet_Amount_Lapel))
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