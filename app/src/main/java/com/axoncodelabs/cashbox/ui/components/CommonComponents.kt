package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

@Composable
fun AppCurrency(
    modifier: Modifier = Modifier,
    style: TextStyle = MyFontStyle.xSmall(),
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    startPadding: Dp = 5.dp
) {
    Text(
        modifier = modifier.padding(start = startPadding),
        text = stringResource(R.string.App_Currency),
        style = style,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

/** > **To disable the click effect.**
 *
 * **How to use [[ .noRippleClickable (enabled){} ]]** **/
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.clickable(
        indication = null,
        interactionSource = interactionSource,
        enabled = enabled
    ) {
        if (enabled) onClick()
    }
}