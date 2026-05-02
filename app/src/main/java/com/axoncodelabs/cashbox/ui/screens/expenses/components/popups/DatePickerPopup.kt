package com.axoncodelabs.cashbox.ui.screens.expenses.components.popups

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.noRippleClickable
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@Composable
fun DatePickerPopup(
    initialDate: Long,
    onDatePickerDismiss: () -> Unit,
    onConfirmBttnClick: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    LaunchedEffect(initialDate) { datePickerState.selectedDateMillis = initialDate }

    val customColors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        headlineContentColor = MaterialTheme.colorScheme.primary,
        dividerColor = MaterialTheme.colorScheme.primary,
        weekdayContentColor = MaterialTheme.colorScheme.primary,
        dayContentColor = MaterialTheme.colorScheme.onBackground,
        selectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
        disabledDayContentColor = MaterialTheme.colorScheme.outline,
        todayContentColor = MaterialTheme.colorScheme.primary
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.background)
    ) {
        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            state = datePickerState,
            showModeToggle = false,
            colors = customColors,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .noRippleClickable { onConfirmBttnClick(System.currentTimeMillis()) }
                    .border(
                        shape = CircleShape,
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(7.dp),
                text = stringResource(R.string.DatePickerTodaySelect),
                style = MyFontStyle.xSmall()
            )
            Spacer(Modifier.weight(1f))

            Text(
                modifier = Modifier.noRippleClickable { onDatePickerDismiss() },
                text = stringResource(R.string.Cancel_Bttn),
                style = MyFontStyle.medium(),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.width(30.dp))

            Text(
                modifier = Modifier.noRippleClickable {
                    datePickerState.selectedDateMillis?.let { onConfirmBttnClick(it) }
                },
                text = stringResource(R.string.Popups_DatePickerConfirm_Bttn),
                style = MyFontStyle.medium(),
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
        Spacer(Modifier.height(15.dp))
    }
}