package com.axoncodelabs.cashbox.ui.components.sheets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

@Composable
fun SheetFieldLabel(
    lapel: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
) {
    Text(
        modifier = modifier,
        text = lapel,
        style = MyFontStyle.medium(),
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

/** @param textAlign > **controls Text position inside the Label Frame by {contentAlignment: Alignment}** */
@Composable
fun SheetRoundedLabel(
    modifier: Modifier = Modifier,
    lapel: String,
    isHideData: Boolean,
    textColor: Color = MaterialTheme.colorScheme.primary,
    /** @param textAlign > **controls Text position inside the Label Frame by {contentAlignment: Alignment}** */
    textAlign: Alignment = Alignment.Center,
    borderColor: Color = MaterialTheme.colorScheme.outline,
) {
    Box(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(vertical = 15.dp, horizontal = 15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        HideTextData(
            isHideData = isHideData,
            text = lapel,
            color = textColor,
            style = MyFontStyle.medium(),
            align = textAlign
        )
    }
}

@Composable
fun SheetTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String = "",
    keyboardType: KeyboardType,
    singleLine: Boolean,
    maxLines: Int? = null,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    hintTextColor: Color = MaterialTheme.colorScheme.outline,
    isError: Boolean = false,
    errorMsg: String = "",
) {
    val actualMaxLines = if (singleLine) 1 else maxLines ?: 5
    val finalBorderColor = if (isError) MaterialTheme.colorScheme.error else borderColor
    val finalValueColor =
        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
    val finalHintColor = if (isError) MaterialTheme.colorScheme.error else hintTextColor

    Column {
        BasicTextField(
            modifier = modifier
                .border(1.dp, finalBorderColor, RoundedCornerShape(10.dp))
                .padding(vertical = 15.dp, horizontal = 15.dp),
            value = value,
            onValueChange = onValueChange,
            textStyle = MyFontStyle.medium().copy(color = finalValueColor),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            maxLines = actualMaxLines,
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hintText,
                        style = MyFontStyle.medium(),
                        color = finalHintColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        )
        if (isError) {
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MyFontStyle.small(),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }
    }
}

@Composable
fun SheetNumField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String,
    singleLine: Boolean,
    maxLines: Int? = null,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    hintTextColor: Color = MaterialTheme.colorScheme.outline,
    isEmptyValue: Boolean = false,
    emptyValueMsg: String = "",
    wrongValueMsg: String = "",
) {
    var showWrongValue by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val actualMaxLines = if (singleLine) 1 else maxLines ?: 5
    val finalBorderColor = when {
        isEmptyValue -> MaterialTheme.colorScheme.error
        showWrongValue -> MaterialTheme.colorScheme.error
        else -> borderColor
    }
    val finalValueColor =
        if (isEmptyValue || showWrongValue) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onBackground
    val finalHintColor =
        if (isEmptyValue || showWrongValue) MaterialTheme.colorScheme.error
        else hintTextColor
    Column {
        BasicTextField(
            modifier = modifier
                .border(1.dp, finalBorderColor, RoundedCornerShape(10.dp))
                .padding(vertical = 15.dp, horizontal = 15.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (isFocused) {
                        val parts = value.split(".")
                        showWrongValue = parts.getOrNull(1)?.length?.let { it > 2 } ?: false
                    } else {
                        showWrongValue = false
                    }
                },
            value = value,
            onValueChange = { newStr ->
                // Allow only digits and one dot
                var filtered = newStr.filter { it.isDigit() || it == '.' }
                val dotCount = filtered.count { it == '.' }
                if (dotCount <= 1) {
                    val parts = filtered.split(".")
                    if (parts.size > 1 && parts[1].length > 2) {
                        filtered = parts[0] + "." + parts[1].take(2)
                    }

                    if (isFocused) {
                        showWrongValue = parts.getOrNull(1)?.length?.let { it > 2 } ?: false
                    }

                    onValueChange(filtered)
                }
            },
            textStyle = MyFontStyle.medium().copy(color = finalValueColor),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = actualMaxLines,
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hintText,
                        style = MyFontStyle.medium(),
                        color = finalHintColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        )
        if (isEmptyValue) {
            Text(
                text = emptyValueMsg,
                color = MaterialTheme.colorScheme.error,
                style = MyFontStyle.small(),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }
        if (showWrongValue) {
            Text(
                text = wrongValueMsg,
                color = MaterialTheme.colorScheme.error,
                style = MyFontStyle.small(),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }
    }
}