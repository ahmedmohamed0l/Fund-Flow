package com.axoncodelabs.fundflow.ui.components.sheets.fundSelection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.ui.components.HideTextData
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundSelectionBttn(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fund: FundEntity?,
    fromFundId: Int? = null,
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
                isHideData = isHideData,
                onSelect = {
                    onFundSelected(it)
                    viewModel.showFunds = false
                },
                fromFundId = fromFundId
            )
        }
    }
    val finalBorderColor = if (isUnSelected) MaterialTheme.colorScheme.error else borderColor

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, finalBorderColor, RoundedCornerShape(10.dp))
                .noRippleClickable { viewModel.showFunds = true }
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HideTextData(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 15.dp),
                isHideData = isHideData,
                text = (fund?.name ?: stringResource(R.string.Sheet_FundSelection)),
                color = fund?.let { textColor } ?: finalBorderColor,
                style = MyFontStyle.medium(),
                align = Alignment.CenterStart
            )
            MyIcons.Arrow(
                autoMirroredState = true,
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