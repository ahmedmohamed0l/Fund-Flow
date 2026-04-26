package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
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

/** @param align > **controls Text position inside the Box by {contentAlignment: Alignment}** */
@Composable
fun HideTextData(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    text: String,
    color: Color,
    style: TextStyle,
    /** @param align > **controls Text position inside the Box by {contentAlignment: Alignment}** */
    align: Alignment = Alignment.Center,
    maxLines: Int = 1,
) {
    val hideBlurState = if (isHideData) (1.5).dp else 0.dp
    Box(
        modifier = modifier.blur(hideBlurState),
        contentAlignment = align
    ) {
        Text(
            text = hideDataMask(
                isHideData,
                text = (text)
            ),
            style = style.merge(TextStyle(textDirection = TextDirection.Ltr)),
            color = color,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}