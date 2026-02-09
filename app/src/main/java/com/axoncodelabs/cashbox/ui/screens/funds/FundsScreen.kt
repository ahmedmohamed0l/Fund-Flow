package com.axoncodelabs.cashbox.ui.screens.funds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.util.doubleFormat
import com.axoncodelabs.cashbox.ui.components.HideTextData
import com.axoncodelabs.cashbox.ui.components.topAppBar.TopBarState
import com.axoncodelabs.cashbox.ui.screens.funds.components.deletepopup.DeleteFundConfirm
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addamount.AddAmountSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addfund.AddFundSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.fundoptions.FundOptionsSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.transfer.TransferSheet
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundsScreen(
    viewModel: FundsViewModel = hiltViewModel(),
    onTopBarChange: (TopBarState) -> Unit,
) {
    //.....( State & ViewModel Setup ).....
    val state = viewModel.state.collectAsState()

    val funds = state.value.funds
    val fundsTotalBalance = state.value.fundsTotalBalance

    val sheet = state.value.currentSheet
    val popup = state.value.popupState

    val isHideData = state.value.isHideData

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    //.....( TopAppBar Data ).....
    LaunchedEffect(Unit) {
        onTopBarChange(
            TopBarState(
                titleRes = R.string.FundsScreen_Identifier,
                showAction = true,
                actionIconRes = R.drawable.ic_add_card,
                onActionClick = {
                    viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheets.AddFund))
                }
            )
        )
    }

    //.....( Sheets & Popups Handling ).....
    @Composable
    fun sheetsHandle() {
        when (sheet) {
            FundsSheets.AddFund -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    AddFundSheet(
                        /*Disable ShowSnackbar
                        snackbarHostState = snackbarHostState,*/
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }

            is FundsSheets.AddAmount -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    AddAmountSheet(
                        isHideData = isHideData,
                        fund = sheet.fund,
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }

            is FundsSheets.Transfer -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    TransferSheet(
                        isHideData = isHideData,
                        fromFund = sheet.fund,
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }

            is FundsSheets.FundOptions -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    FundOptionsSheet(
                        isHideData = isHideData,
                        fund = sheet.fund,
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }

            FundsSheets.None -> Unit
        }
    }
    sheetsHandle()

    @Composable
    fun popupsHandle() {
        when (popup) {
            is FundsPopup.DeleteFund -> {
                Dialog(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.ClosePopup) }
                ) {
                    DeleteFundConfirm(
                        fund = popup.fund,
                        onCancel = { viewModel.onEvent(FundsEvent.ClosePopup) },
                    )
                }
            }

            FundsPopup.Close -> Unit
        }
    }
    popupsHandle()

    //.....( Screen Layout ).....
    FundsScreenRoot(
        funds = funds,
        isHideData = isHideData,
        onEvent = viewModel::onEvent,
        fundsTotalBalance = fundsTotalBalance
    )
}

/**.....( Screen Layout ).....**/
@Composable
private fun FundsScreenRoot(
    funds: List<FundEntity>,
    isHideData: Boolean,
    onEvent: (FundsEvent) -> Unit,
    fundsTotalBalance: Double
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            FundsPageState(
                funds = funds,
                isHideData = isHideData,
                onEvent = onEvent
            )

            TotalFundsValue(
                modifier = Modifier.align(Alignment.TopCenter),
                isHideData = isHideData,
                fundsTotalBalance = fundsTotalBalance
            )
        }
    }
}

/** --------------------[ Components ]-------------------- **/

@Composable
private fun TotalFundsValue(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fundsTotalBalance: Double
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
            .height(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .border(
                1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.FundsScreen_FundsTotal),
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 30.dp)
                .weight(5f)
        )
        Box(modifier = Modifier.weight(5f), contentAlignment = Alignment.CenterEnd) {
            HideTextData(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 30.dp),
                isHideData = isHideData,
                text = (doubleFormat(fundsTotalBalance)),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.large()
            )
        }
    }

}

@Composable
private fun FundsPageState(
    funds: List<FundEntity>,
    isHideData: Boolean,
    onEvent: (FundsEvent) -> Unit
) {
    if (funds == emptyList()) {
        EmptyFundsPage()
    } else {
        FundsList(
            funds = funds,
            isHideData = isHideData,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun EmptyFundsPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(130.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.FundsScreen_EmptyFundsPage_Title),
            style = MyFontStyle.largeBold(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 28.sp
        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .scale(1f)
                .size(300.dp),
            painter = painterResource(id = R.drawable.img_empty_box),
            contentDescription = "comfort"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = stringResource(id = R.string.FundsScreen_EmptyFundsPage_Description),
            style = MyFontStyle.medium(),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun FundsList(
    funds: List<FundEntity>,
    isHideData: Boolean,
    onEvent: (FundsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(MyRoundedCornerShape.medium)
        ) {
            item { Spacer(modifier = Modifier.height(70.dp)) }
            itemsIndexed(
                items = funds,
                key = { _, fund -> fund.id },
                contentType = { _, _ -> "FundItem" }) { index, fund ->
                FundItem(
                    isHideData = isHideData, fund = fund, onEvent = onEvent
                )
                if (index != funds.lastIndex) {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            item { Spacer(modifier = Modifier.height(75.dp)) }
        }
    }
}

@Composable
private fun FundItem(
    isHideData: Boolean,
    fund: FundEntity,
    onEvent: (FundsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = MyRoundedCornerShape.medium,
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background),
                modifier = modifier
                    .matchParentSize()
                    .alpha(0.6f),
            )
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = modifier.weight(20f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyIcons.Settings(
                            filledState = false,
                            size = 25.dp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onEvent(FundsEvent.SheetDisplayed(FundsSheets.FundOptions(fund)))
                            })
                        Spacer(modifier = modifier.width(15.dp))

                        HideTextData(
                            isHideData = isHideData,
                            text = (fund.name),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MyFontStyle.medium()
                        )
                    }
                    Box(modifier = Modifier.weight(10f), contentAlignment = Alignment.CenterEnd) {
                        HideTextData(
                            isHideData = isHideData,
                            text = (doubleFormat(fund.balance)),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MyFontStyle.large()
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.FundsScreen_AddBalance_Bttn),
                        color = MaterialTheme.colorScheme.inversePrimary,
                        style = MyFontStyle.small(),
                        modifier = modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onEvent(
                                FundsEvent.SheetDisplayed(FundsSheets.AddAmount(fund))
                            )
                        })
                    Text(
                        text = stringResource(R.string.FundsScreen_FundsTransfer_Bttn),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MyFontStyle.small(),
                        modifier = modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onEvent(
                                FundsEvent.SheetDisplayed(FundsSheets.Transfer(fund))
                            )
                        })
                    Text(
                        text = stringResource(R.string.FundsScreen_DeleteFunds_Bttn),
                        color = MaterialTheme.colorScheme.error,
                        style = MyFontStyle.small(),
                        modifier = modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onEvent(
                                FundsEvent.PopupDisplay(FundsPopup.DeleteFund(fund))
                            )
                        })
                }
            }
        }
    }
}