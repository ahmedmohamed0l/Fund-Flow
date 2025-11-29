package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

@Composable
fun MyTopAppBar(
    barColor: Color = MaterialTheme.colorScheme.primary,
    onBarColor: Color = MaterialTheme.colorScheme.onPrimary,
    title: String,

    showAction: Boolean = false,
    actionIcon: Painter? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(barColor)
    ) {
        Text(
            text = title,
            color = onBarColor,
            style = MyFontStyle.mediumBold(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 20.dp),
            textAlign = TextAlign.Center
        )
        if (showAction && actionIcon != null && onActionClick != null) {
            IconButton(
                onClick = onActionClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    painter = actionIcon,
                    contentDescription = "Action",
                    tint = onBarColor
                )
            }
        }
    }
}