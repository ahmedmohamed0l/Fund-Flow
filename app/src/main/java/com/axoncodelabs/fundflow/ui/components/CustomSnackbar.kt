package com.axoncodelabs.fundflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    backgroundAlpha: Float = 0.8f
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = backgroundAlpha))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = snackbarData.visuals.message,
            color = MaterialTheme.colorScheme.onTertiary,
            style = MyFontStyle.medium()
        )
    }
}