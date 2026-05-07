package com.axoncodelabs.fundflow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons
import com.axoncodelabs.fundflow.ui.util.DateFormates
import com.axoncodelabs.fundflow.ui.util.dateFormatter

@Composable
fun DateSelection(
    onDateIncrease: () -> Unit,
    date: Long,
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
                    .offset(y = (-2.5).dp)
                    .noRippleClickable { onDateDecrease() }
            )
            Text(
                text = date.dateFormatter(DateFormates.FullNumDateArabic),
                style = MyFontStyle.medium(),
                color = dateColor
            )
            MyIcons.Arrow(
                autoMirroredState = true,
                size = 25.dp, color = arrowColor,
                modifier = Modifier
                    .offset(y = (-2.5).dp)
                    .noRippleClickable { onDateIncrease() }
            )
        }
    }
}