package com.axoncodelabs.cashbox.templates

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.theme.MyColors
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@Composable
fun ReportCards(
    dayExpensesTotal: Double = 0.0,
    dayExpense: Double = 0.0,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .border(1.dp, MyColors.CyanOff, RoundedCornerShape(8.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = MyRoundedCornerShape.small,
        onClick = { expandedState = !expandedState }
    ) {
        Column(
            modifier = Modifier
                .background(MyColors.White)
                .padding(horizontal = 5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(9f),
                    text = stringResource(id = R.string.Reports_Card_expensesDate),
                    textAlign = TextAlign.Start,
                    color = MyColors.CyanOff,
                    style = MyFontStyle.medium(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .weight(2f),
                    text = "$dayExpensesTotal",
                    textAlign = TextAlign.Center,
                    color = MyColors.CyanOff,
                    style = MyFontStyle.medium(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = { expandedState = !expandedState },
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationState)
                ) {

                    MyIcons.Arrow(
                        angle = 90f,
                        size = 15.dp,
                        color = MyColors.MidGreen,
                        autoMirroredState = false
                    )
                }
            }
            if (expandedState) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    CardExpense(dayExpense)
                    CardExpense(dayExpense)
                    CardExpense(dayExpense)
                    CardExpense(dayExpense)
                }
            }
        }
    }
}

@Composable
private fun CardExpense(dayExpense: Double) {
    val showEditExpenseDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    if (showEditExpenseDialog.value) {
        Dialog(
            onDismissRequest = {
                showEditExpenseDialog.value = false
            }
        ) {
            //EditExpense()
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { showEditExpenseDialog.value = true }
    ) {

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(2f),
            text = stringResource(id = R.string.Reports_Card_expensesDate),
            style = MyFontStyle.medium(),
            maxLines = 1,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = "$dayExpense",
            textAlign = TextAlign.Center,
            style = MyFontStyle.medium(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(2f),
            text = stringResource(id = R.string.ChoseFund),
            textAlign = TextAlign.End,
            style = MyFontStyle.medium(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}