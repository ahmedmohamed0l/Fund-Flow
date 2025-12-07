package com.axoncodelabs.cashbox.ui.screens.funds

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar
import com.axoncodelabs.cashbox.ui.screens.funds.components.addamount.AddAmountSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.addfund.AddFundSheet
import com.axoncodelabs.cashbox.ui.screens.funds.components.deletepopup.DeleteFundConfirm
import com.axoncodelabs.cashbox.ui.screens.funds.components.transfer.TransferSheet
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.cashbox.ui.theme.doubleFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundsScreen(
    viewModel: FundsViewModel = hiltViewModel(),
) {
    val funds = viewModel.funds.collectAsState(initial = emptyList())
    val fundsTotalBalance = viewModel.fundsTotalBalance.collectAsState(initial = 0.0)

    val state = viewModel.state.collectAsState()
    val sheet = state.value.currentSheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val popup = state.value.popupState


    /*Disable ShowSnackbar
    val snackbarHostState = remember { SnackbarHostState() }*/
    Scaffold(
        /*Disable ShowSnackbar
        snackbarHost = { SnackbarHost(snackbarHostState) },*/
        topBar = {
            MyTopAppBar(
                title = stringResource(id = R.string.FundsScreen_Identifier),
                showAction = true,
                actionIcon = painterResource(id = R.drawable.ic_add_card),
                onActionClick = { viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheet.AddFund)) })
        }) { inner ->
        when (sheet) {
            is FundsSheet.AddFund -> {
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

            is FundsSheet.AddAmount -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    AddAmountSheet(
                        fund = sheet.fund,
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }
            is FundsSheet.Transfer -> {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onEvent(FundsEvent.CloseSheet) },
                    containerColor = MaterialTheme.colorScheme.background,
                    sheetState = sheetState
                ) {
                    TransferSheet(
                        fromFund = sheet.fund,
                        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) }
                    )
                }
            }

            FundsSheet.None -> Unit
            else -> Unit
        }

        when (popup) {
            is FundsPopup.Open -> {
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


        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(29.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                Text(
                    text = doubleFormat(fundsTotalBalance.value),
                    style = MyFontStyle.large(),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp)
                    .clip(MyRoundedCornerShape.medium)
            ) {
                items(funds.value) { fund ->
                    FundItem(
                        fund = fund, onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun FundItem(
    fund: FundEntity,
    onEvent: (FundsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.tertiary)
            .height(160.dp)
            .padding(20.dp)
            .padding(vertical = 10.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = modifier.align(Alignment.CenterStart),
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
                        onEvent(FundsEvent.SheetDisplayed(FundsSheet.FundOptions(fund)))
                    })
                Spacer(modifier = modifier.width(15.dp))
                Text(
                    text = fund.name,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MyFontStyle.medium()
                )
            }
            Text(
                text = doubleFormat(fund.balance),
                color = MaterialTheme.colorScheme.onTertiary,
                style = MyFontStyle.large(),
                modifier = modifier.align(Alignment.CenterEnd)
            )
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
                        FundsEvent.SheetDisplayed(FundsSheet.AddAmount(fund))
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
                            FundsSheet.Transfer(
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
                        FundsEvent.PopupDisplay(FundsPopup.Open(fund))
                    )
                })
        }
    }
}