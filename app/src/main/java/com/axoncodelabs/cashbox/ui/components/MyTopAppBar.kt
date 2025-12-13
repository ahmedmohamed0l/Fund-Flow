package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

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
            .height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(barColor)
        ) {}
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(MyRoundedCornerShape.large)
                .background(barColor)
        ) {}
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
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onActionClick() }
                        .padding(10.dp),
                ) {
                    Icon(
                        painter = actionIcon,
                        contentDescription = "Action",
                        tint = onBarColor,

                        )
                }
                VerticalDivider(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 5.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    thickness = 1.dp,
                    color = onBarColor.copy(alpha = 0.5f)
                )
            }
        }
    }
}

/**--------------------[ Preview ]--------------------**/
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = false
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            MyTopAppBar(
                title = "المصروفات"
            )
        }
    }
}