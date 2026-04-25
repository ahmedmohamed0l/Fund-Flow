package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons

@Composable
fun DateSelection(
    onDateIncrease: () -> Unit,
    date: String,
    onDateDecrease: () -> Unit,
    identifierColor: Color = MaterialTheme.colorScheme.onBackground,
    dateColor: Color = MaterialTheme.colorScheme.primary,
    arrowColor: Color = MaterialTheme.colorScheme.outline,
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = stringResource(id = R.string.Sheet_TransactionDateSelector),
            style = MyFontStyle.medium(),
            color = identifierColor
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcons.Arrow(
                autoMirroredState = true,
                size = 25.dp, angle = 180f, color = arrowColor,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDateDecrease() }
            )
            Text(
                text = date,
                style = MyFontStyle.medium(),
                color = dateColor
            )
            MyIcons.Arrow(
                autoMirroredState = true,
                size = 25.dp, color = arrowColor,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDateIncrease() }
            )
        }
    }
}