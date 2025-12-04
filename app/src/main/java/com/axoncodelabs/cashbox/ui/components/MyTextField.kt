package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

/*          ----------(Usage)----------


    val textState = rememberSaveable { mutableStateOf("") }
    MyTextField(
        modifier = Modifier.align(Alignment.Center),
        value = textState.value,
        onValueChange = { textState.value = it },
        hintText = "Hint",
        keyboardType = KeyboardType.Number,
        singleLine = true,
        /**--[if {singleLine = false} modify ur custom maxLines]--**/
        //maxLines = 10 )

*/
@Composable
fun MyLabel(
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

@Composable
fun MyRoundedLabel(
    lapel: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
    borderColor: Color = MaterialTheme.colorScheme.outline,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(vertical = 15.dp, horizontal = 15.dp),
    ) {
        Text(
            modifier = modifier.align(Alignment.CenterStart),
            text = lapel,
            style = MyFontStyle.medium(),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String,
    keyboardType: KeyboardType,
    singleLine: Boolean,
    maxLines: Int? = null,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    hintTextColor: Color = MaterialTheme.colorScheme.outline,
    isError: Boolean = false,
    errorMsg: String = "",
) {
    val actualMaxLines = if (singleLine) 1 else maxLines ?: 5
    val finalBorderColor = if (isError) MaterialTheme.colorScheme.error else borderColor
    val finalValueColor =
        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSecondary
    val finalHintColor = if (isError) MaterialTheme.colorScheme.error else hintTextColor

    Column() {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, finalBorderColor, RoundedCornerShape(10.dp))
                .padding(vertical = 15.dp, horizontal = 15.dp),
            value = value,
            onValueChange = onValueChange,
            textStyle = MyFontStyle.medium().copy(color = finalValueColor),
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