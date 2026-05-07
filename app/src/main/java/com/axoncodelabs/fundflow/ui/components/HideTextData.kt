package com.axoncodelabs.fundflow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HideTextData(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    text: String,
    color: Color,
    style: TextStyle,
    /** @param align > **controls Text position inside the Box by {contentAlignment: Alignment}** */
    align: Alignment = Alignment.Center,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = 1,
) {
    val hideBlurState = if (isHideData) (1.5).dp else 0.dp
    Box(
        modifier = modifier.blur(hideBlurState),
        contentAlignment = align
    ) {
        Text(
            modifier = Modifier.padding(vertical = 2.dp),
            text = hideDataMask(
                isHideData,
                text = (text)
            ),
            style = style,
            color = color,
            maxLines = maxLines,
            textAlign = textAlign,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun hideDataMask(isHide: Boolean, text: String): String {
    return if (isHide) {
        "x".repeat(text.length)
    } else {
        text
    }
}