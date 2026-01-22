package com.axoncodelabs.cashbox.ui.components.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

@Composable
fun MyTopAppBar(
    state: TopBarState,
    barColor: Color = MaterialTheme.colorScheme.primary,
    onBarColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Card(
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = barColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp)
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = state.titleRes),
                color = onBarColor,
                style = MyFontStyle.mediumBold(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )
            if (state.showAction && state.actionIconRes != null && state.onActionClick != null) {
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
                            ) { state.onActionClick.invoke() }
                            .padding(10.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = state.actionIconRes),
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
}