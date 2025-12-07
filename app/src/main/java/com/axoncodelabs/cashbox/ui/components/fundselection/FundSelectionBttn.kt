package com.axoncodelabs.cashbox.ui.components.fundselection

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundSelectionBttn(
    modifier: Modifier = Modifier,
    fund: FundEntity?,
    fromFundId: Int,
    onFundSelected: (FundEntity) -> Unit,
    viewModel: FundSelectionVM = hiltViewModel(),
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    isUnSelected: Boolean = false,
    unSelectedErrorMsg: String = "",
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (viewModel.showFunds) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.showFunds = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            FundSelectionSheet(
                onSelect = {
                    onFundSelected(it)
                    viewModel.showFunds = false
                },
                fromFundId = fromFundId
            )
        }
    }

    val finalBorderColor = if (isUnSelected) {
        MaterialTheme.colorScheme.error
    } else {
        borderColor
    }

    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, finalBorderColor, RoundedCornerShape(10.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { viewModel.showFunds = true }
                .padding(horizontal = 15.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(vertical = 15.dp),
                text = fund?.name ?: stringResource(R.string.Sheet_FundSelection),
                style = MyFontStyle.medium(),
                color = fund?.let { textColor } ?: finalBorderColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            MyIcons.Arrow(
                autoMirroredState = false,
                modifier = Modifier.align(Alignment.CenterEnd),
                size = 25.dp, color = finalBorderColor, angle = 180f
            )
        }
        if (isUnSelected) {
            Text(
                text = unSelectedErrorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MyFontStyle.small(),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }

    }
}