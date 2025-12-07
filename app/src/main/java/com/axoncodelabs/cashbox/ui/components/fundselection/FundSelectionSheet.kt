package com.axoncodelabs.cashbox.ui.components.fundselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.theme.doubleFormat

@Composable
fun FundSelectionSheet(
    onSelect: (FundEntity) -> Unit,
    fromFundId: Int,
    viewModel: FundSelectionVM = hiltViewModel(),
) {
    val funds = viewModel.funds.collectAsState(initial = emptyList())
    val filteredFunds = funds.value.filter { it.id != fromFundId }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(MyRoundedCornerShape.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredFunds) { fund ->
                FundItem(
                    fund = fund,
                    onSelect = { onSelect(fund) }
                )
                if (fund != filteredFunds.last()) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clip(MyRoundedCornerShape.large),
                        thickness = (0.5).dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun FundItem(
    fund: FundEntity,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcons.CreditCard(
                size = 25.dp,
                color = MaterialTheme.colorScheme.outline,
            )
            Spacer(modifier = modifier.width(7.dp))
            Text(
                text = fund.name,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MyFontStyle.medium()
            )
        }

        Text(
            text = doubleFormat(fund.balance),
            color = MaterialTheme.colorScheme.onTertiary,
            style = MyFontStyle.large()
        )
    }
}