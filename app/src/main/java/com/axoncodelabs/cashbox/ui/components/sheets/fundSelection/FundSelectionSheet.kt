package com.axoncodelabs.cashbox.ui.components.sheets.fundSelection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.components.AppCurrency
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.noRippleClickable
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.util.formatAmount

@Composable
fun FundSelectionSheet(
    isHideData: Boolean,
    onSelect: (FundEntity) -> Unit,
    fromFundId: Int? = null,
    viewModel: FundSelectionVM = hiltViewModel(),
) {
    val funds = viewModel.funds.collectAsState(initial = emptyList())
    val displayedFunds = if (fromFundId != null) {
        funds.value.filter { it.id != fromFundId }
    } else {
        funds.value
    }

    if (funds.value.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.Sheet_EmptyFundList),
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
                items(displayedFunds) { fund ->
                    FundItem(
                        isHideData = isHideData,
                        fund = fund,
                        onSelect = { onSelect(fund) }
                    )
                    if (fund != displayedFunds.last()) {
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
private fun FundItem(
    isHideData: Boolean,
    fund: FundEntity,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(10.dp)
            .noRippleClickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcons.CreditCard(
                modifier = Modifier.offset(y = (-2.5).dp),
                size = 25.dp,
                color = MaterialTheme.colorScheme.outline,
            )
            Spacer(modifier = Modifier.width(7.dp))
            HideTextData(
                isHideData = isHideData,
                text = (fund.name),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium(),
                align = Alignment.CenterStart
            )
        }
        HideTextData(
            modifier = Modifier.weight(1f),
            isHideData = isHideData,
            text = fund.balance.formatAmount(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.large(),
            align = Alignment.CenterEnd
        )
        AppCurrency(textColor = MaterialTheme.colorScheme.onBackground)
    }
}