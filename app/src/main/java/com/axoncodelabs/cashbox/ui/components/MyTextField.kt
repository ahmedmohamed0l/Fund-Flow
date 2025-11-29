package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String,
    keyboardType: KeyboardType,
    singleLine: Boolean,
    maxLines: Int? = null,
) {
    val actualMaxLines = if (singleLine) 1 else maxLines ?: 5

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 15.dp),
        value = value,
        onValueChange = onValueChange,
        textStyle = MyFontStyle.medium(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        maxLines = actualMaxLines,
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = hintText,
                    style = MyFontStyle.medium(),
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            innerTextField()
        }
    )
}