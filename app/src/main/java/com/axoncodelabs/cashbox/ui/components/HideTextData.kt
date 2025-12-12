package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** [ Quick Use ] **/
// hideDataMask(isHideData = ,text = )

fun hideDataMask(isHide: Boolean, text: String): String {
    return if (isHide) {
        "x".repeat(text.length)
    } else {
        text
    }
}

/** [ Quick Use ] **/
/*
        HideTextData(
            modifier = Modifier,
            isHideData = ,
            text = ,
            color = ,
            style =
        )
*/

@Composable
fun HideTextData(
    isHideData: Boolean,
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    val hideBlurState = if (isHideData) (1.5).dp else 0.dp
    Box(
        modifier = modifier
            .wrapContentSize()
            .blur(hideBlurState)
    ) {
        Text(
            text = hideDataMask(
                isHideData,
                text = (text)
            ),
            style = style,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}