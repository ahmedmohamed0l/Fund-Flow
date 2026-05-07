package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

/*For quick use
    var isShowSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    if (isShowSnackbar) {
            CustomSnackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp),
                message = snackbarMessage
            )
            LaunchedEffect(isShowSnackbar) {
                delay(2500)
                isShowSnackbar = false
            }
        }
*/

@Composable
fun CustomSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    backgroundAlpha: Float = 0.7f
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = backgroundAlpha))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onTertiary,
            style = MyFontStyle.medium()
        )
    }
}