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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar
import com.axoncodelabs.cashbox.ui.screens.funds.components.deletepopup.DeleteFundConfirm
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addamount.AddAmountSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.addfund.AddFundSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.fundoptions.FundOptionsSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.sheets.transfer.TransferSheet
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.theme.doubleFormat
import com.axoncodelabs.cashbox.ui.theme.hideDataMask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundsScreen(
    viewModel: FundsViewModel = hiltViewModel(),
) {
    //.....( State & ViewModel Setup ).....
    val funds = viewModel.funds.collectAsState(initial = emptyList())
    val fundsTotalBalance = viewModel.fundsTotalBalance.collectAsState(initial = 0.0)

    val state = viewModel.state.collectAsState()
    val sheet = state.value.currentSheet
    val popup = state.value.popupState

    val isHideData = state.value.isHideData!!
    val hideBlurState = if (isHideData) (1.5).dp else 0.dp

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    //.....( Sheets & Popups Handling ).....
    @Composable
    fun sheetsHandle() {
        when (sheet) {
            is FundsSheets.AddFund -> {
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
                        hideBlurState = hideBlurState,
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
                        hideBlurState = hideBlurState,
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
                        hideBlurState = hideBlurState,
                        isHideData = isHideData,
                        fund = sheet.fund,
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }

            FundsSheets.None -> Unit
        }
    }

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


    //.....( Screen Layout ).....

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        sheetsHandle()
        popupsHandle()

        MyTopAppBar(
            title = stringResource(id = R.string.FundsScreen_Identifier),
            showAction = true,
            actionIcon = painterResource(id = R.drawable.ic_add_card),
            onActionClick = { viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheets.AddFund)) }
        )
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(60.dp)
                .clip(MyRoundedCornerShape.medium)
                .border(1.dp, MaterialTheme.colorScheme.primary, MyRoundedCornerShape.medium)
                .background(MaterialTheme.colorScheme.onPrimaryFixed)
        ) {
            Text(
                stringResource(R.string.FundsScreen_FundsTotal),
                style = MyFontStyle.medium(),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp)
                    .wrapContentSize()
                    .blur(hideBlurState)
            ) {
                Text(
                    text = hideDataMask(
                        isHideData,
                        text = (doubleFormat(fundsTotalBalance.value))
                    ),
                    style = MyFontStyle.large(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MyRoundedCornerShape.medium)
            ) {
                items(funds.value) { fund ->
                    FundItem(
                        hideBlurState = hideBlurState,
                        isHideData = isHideData,
                        fund = fund,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun FundItem(
    hideBlurState: Dp,
    isHideData: Boolean?,
    fund: FundEntity,
    onEvent: (FundsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.secondary)
            .height(160.dp)
    ) {
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
                .clip(MyRoundedCornerShape.medium)
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

                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .blur(hideBlurState)
                    ) {
                        Text(
                            text = hideDataMask(isHideData, text = (fund.name)),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MyFontStyle.medium()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .blur(hideBlurState)
                ) {
                    Text(
                        text = hideDataMask(isHideData, text = (doubleFormat(fund.balance))),
                        color = MaterialTheme.colorScheme.onTertiary,
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
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MyFontStyle.small(),
                    modifier = modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onEvent(
                            FundsEvent.SheetDisplayed(
                                FundsSheets.Transfer(
                                    fund
                                )
                            )
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