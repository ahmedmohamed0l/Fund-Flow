package com.axoncodelabs.cashbox.ui.screens.reports.componants.monthSelectionSheet


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.screens.reports.formatDate
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@Composable
fun MonthSelectionSheet(
    onSelect: (Long) -> Unit,
    selectedDate: Long? = null,
    viewModel: MonthSelectionVM = hiltViewModel(),
) {
    val dates = viewModel.dates.collectAsState(initial = emptyList())
    val displayedDates = if (selectedDate != null) {
        dates.value.filter { it != selectedDate }
    } else {
        dates.value
    }

    if (dates.value.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.Sheet_EmptyDateList),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(displayedDates) { date ->
                    DateItem(
                        date = date,
                        onSelect = { onSelect(date) }
                    )
                    if (date != displayedDates.last()) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clip(MyRoundedCornerShape.large),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateItem(
    date: Long,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(10.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onSelect() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(20f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcons.Time_Zone(
                size = 25.dp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = formatDate(date),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium()
            )
        }
    }
}