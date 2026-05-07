package com.axoncodelabs.fundflow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle

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

// ────────( Empty Page )────────
@Composable
fun EmptyPage(
    modifier: Modifier = Modifier,
    ifImageFirst: Boolean = false,
    topText: String,
    image: Painter,
    imageScale: Float,
    imageSize: Dp,
    bottomText: String,
    bottomSpace: Dp
) {
    Column(
        modifier = modifier.padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (ifImageFirst) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(imageScale)
                    .size(imageSize),
                painter = image,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = topText,
                style = MyFontStyle.largeBold(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = topText,
                style = MyFontStyle.largeBold(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 28.sp
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(imageScale)
                    .size(imageSize),
                painter = image,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = bottomText,
            style = MyFontStyle.medium(),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(bottomSpace))
    }
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