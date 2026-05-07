package com.axoncodelabs.fundflow.ui.screens.reports.componants.monthPickerPopup

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.ui.components.MainBttn
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyRoundedCornerShape
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs


@Composable
fun ScrollableMonthPicker(
    viewModel: MonthPickerVM = hiltViewModel(),
    initialDateMillis: Long?,
    onDateConfirmed: (Long) -> Unit
) {
    val years by viewModel.availableYears.collectAsState()
    val monthNames by viewModel.availableMonthNames.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonthIndex by viewModel.selectedMonthIndex.collectAsState()
    val confirmedDate by viewModel.confirmedDateMillis.collectAsState()

    LaunchedEffect(confirmedDate) {
        confirmedDate?.let {
            onDateConfirmed(it)
            viewModel.clearConfirmedDate()
        }
    }

    LaunchedEffect(initialDateMillis) {
        viewModel.setInitialDate(initialDateMillis)
    }

    if (years.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(500.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No transactions available")
        }
        return
    }

    val safeSelectedYear = selectedYear ?: years.first()
    val safeMonthIndex = selectedMonthIndex.coerceIn(0, monthNames.lastIndex)

    ScrollableMonthPickerRoot(
        yearRange = years,
        monthRange = monthNames,
        selectedMonthIndex = safeMonthIndex,
        selectedYear = safeSelectedYear,
        onMonthChanged = viewModel::onMonthIndexSelected,
        onYearChanged = viewModel::onYearSelected,
        onConfirmBttnClicked = viewModel::onConfirmSelection
    )
}

@Composable
fun ScrollableMonthPickerRoot(
    yearRange: List<Int>,
    monthRange: List<String>,
    selectedMonthIndex: Int,
    selectedYear: Int,
    onMonthChanged: (Int) -> Unit,
    onYearChanged: (Int) -> Unit,
    onConfirmBttnClicked: () -> Unit
) {
    val yearStrings = remember(yearRange) { yearRange.map { it.toString() } }
    val yearIndex = yearRange.indexOf(selectedYear).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MyRoundedCornerShape.extraLarge)
            .background(colorScheme.background)
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //── Headers ────
        Box(
            modifier = Modifier
                .clip(MyRoundedCornerShape.medium)
                .border(
                    width = 1.dp,
                    shape = MyRoundedCornerShape.medium,
                    color = colorScheme.primary.copy(alpha = 0.3f)
                )
                .background(colorScheme.primary.copy(alpha = 0.1f))
                .padding(10.dp)
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.Popups_MonthPickerTitle),
                style = MyFontStyle.xxxLarge(),
                color = colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.Popups_MonthPicker_Month),
                modifier = Modifier.weight(1f),
                color = colorScheme.primary,
                style = MyFontStyle.xLarge(),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.Popups_MonthPicker_Year),
                modifier = Modifier.weight(1f),
                color = colorScheme.primary,
                style = MyFontStyle.xLarge(),
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(10.dp))

        //── Two scroll wheels ────
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = colorScheme.primary.copy(alpha = 0.3f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // month wheel
            ScrollWheelPicker(
                items = monthRange,
                selectedIndex = selectedMonthIndex.coerceIn(0, monthRange.lastIndex),
                onItemSelected = onMonthChanged,
                label = { it },
                modifier = Modifier.weight(1f)
            )

            // year wheel
            ScrollWheelPicker(
                items = yearStrings,
                selectedIndex = yearIndex,
                onItemSelected = { onYearChanged(yearRange[it]) },
                label = { it },
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = colorScheme.primary.copy(alpha = 0.3f)
        )

        //── Confirm button ────
        Spacer(Modifier.height(15.dp))
        MainBttn(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.Popups_DatePickerConfirm_Bttn),
            onClick = onConfirmBttnClicked
        )

    }
}

// ── Generic Scroll Wheel ───────────────────────────────────────────────────────
@Composable
private fun <T> ScrollWheelPicker(
    items: List<T>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 56.dp,
    visibleItemsCount: Int = 3
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    val centeredIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val center = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

            layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - center)
            }?.index ?: 0
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { centeredIndex }
            .distinctUntilChanged()
            .collect { newCentered ->
                if (newCentered != selectedIndex) {
                    onItemSelected(newCentered)
                }
            }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItemsCount)
    ) {
        // highlight band
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
                .clip(MyRoundedCornerShape.large)
                .background(colorScheme.primary.copy(alpha = 0.3f))
                .border(2.dp, colorScheme.primary, MyRoundedCornerShape.large)
        )

        val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight)
        ) {
            itemsIndexed(items, key = { index, _ -> index }) { index, item ->
                val dist = abs(index - centeredIndex)

                val targetScale = when (dist) {
                    0 -> 1.2f
                    1 -> 1f
                    else -> 0.85f
                }
                val targetAlpha = when (dist) {
                    0 -> 1f
                    1 -> 0.45f
                    else -> 0.15f
                }

                val animatedScale by animateFloatAsState(targetValue = targetScale)
                val animatedAlpha by animateFloatAsState(targetValue = targetAlpha)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .alpha(animatedAlpha)
                        .scale(animatedScale),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label(item),
                        color = if (dist == 0) colorScheme.primary else colorScheme.onSecondary,
                        style = if (dist == 0) MyFontStyle.xxxLargeSimiBold() else MyFontStyle.large(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}