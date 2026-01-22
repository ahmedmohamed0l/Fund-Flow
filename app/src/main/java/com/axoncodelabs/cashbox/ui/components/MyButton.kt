package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild

@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun MyBlurredButton(
    modifier: Modifier,
    text: String,
    hazeState: HazeState,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MyRoundedCornerShape.large)
            .hazeChild(
                state = hazeState,
                shape = MyRoundedCornerShape.large,
                style = HazeStyle(
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    blurRadius = 10.dp,
                    noiseFactor = 10f
                )
            )
            .border(
                (0.5).dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                MyRoundedCornerShape.large
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}