package com.axoncodelabs.fundflow.ui.screens.funds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.ui.components.AppCurrency
import com.axoncodelabs.fundflow.ui.components.EmptyPage
import com.axoncodelabs.fundflow.ui.components.HideTextData
import com.axoncodelabs.fundflow.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.components.popups.DeletePopup
import com.axoncodelabs.fundflow.ui.components.popups.PopupResult
import com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.addAmount.AddAmountSheet
import com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.addFund.AddFundSheet
import com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.fundOptions.FundOptionsSheet
import com.axoncodelabs.fundflow.ui.screens.funds.components.sheets.transfer.TransferSheet
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons
import com.axoncodelabs.fundflow.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.fundflow.ui.util.formatAmount

@Composable
fun FundsScreen(
    viewModel: FundsViewModel = hiltViewModel(),
    onTopBarChange: (AppTopBarState) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    //──── State & ViewModel Setup ────
    val state by viewModel.state.collectAsState()

    val sheet = state.currentSheet
    val popup = state.popupState
    val isHideData = state.isHideData

    val funds = state.funds
    val fundsTotalBalance = state.fundsTotalBalance

    //──── AppTopBar Data ────
    SideEffect {
        onTopBarChange(
            AppTopBarState(
                titleRes = R.string.FundsScreen_Identifier,
                showAction = true,
                actionIconRes = R.drawable.ic_add_card,
                onActionClick = {
                    viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheets.AddFund))
                }
            )
        )
    }

    //──── Sheets & Popups Handling ────
    SheetsHandler(
        sheet = sheet,
        isHideData = isHideData,
        onClose = { viewModel.onEvent(FundsEvent.CloseSheet) },
        onShowSnackbar = onShowSnackbar
    )

    PopupsHandler(
        popup = popup,
        onClose = { viewModel.onEvent(FundsEvent.ClosePopup) },
        onDeleteFund = { fund ->
            viewModel.onEvent(FundsEvent.DeleteFund(fund))
        },
        onShowSnackbar = onShowSnackbar
    )

    //──── Screen Layout ────
    FundsScreenRoot(
        isHideData = isHideData,

        fundsTotalBalance = fundsTotalBalance,
        funds = funds,

        onFundOptionsClick = {
            viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheets.FundOptions(it)))
        },
        onAddAmountClick = {
            viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheets.AddAmount(it)))
        },
        onTransferClick = {
            viewModel.onEvent(FundsEvent.SheetDisplayed(FundsSheets.Transfer(it)))
        },
        onDeleteFundClick = {
            viewModel.onEvent(FundsEvent.PopupDisplay(FundsPopup.DeleteFund(it)))
        }
    )
}

// ────────────────{ Handlers }────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetsHandler(
    sheet: FundsSheets,
    isHideData: Boolean,
    onClose: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (sheet != FundsSheets.None) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            containerColor = MaterialTheme.colorScheme.background,
            sheetState = sheetState
        ) {
            when (sheet) {
                FundsSheets.AddFund -> {
                    AddFundSheet(
                        onClose = onClose,
                        onShowSnackbar = onShowSnackbar
                    )
                }

                is FundsSheets.FundOptions -> {
                    FundOptionsSheet(
                        isHideData = isHideData,
                        fund = sheet.fund,
                        onClose = onClose,
                        onShowSnackbar = onShowSnackbar
                    )
                }

                is FundsSheets.AddAmount -> {
                    AddAmountSheet(
                        isHideData = isHideData,
                        fund = sheet.fund,
                        onClose = onClose,
                        onShowSnackbar = onShowSnackbar
                    )
                }

                is FundsSheets.Transfer -> {
                    TransferSheet(
                        isHideData = isHideData,
                        fromFund = sheet.fund,
                        onClose = onClose,
                        onShowSnackbar = onShowSnackbar
                    )
                }

                FundsSheets.None -> Unit
            }
        }
    }
}

@Composable
private fun PopupsHandler(
    popup: FundsPopup,
    onClose: () -> Unit,
    onDeleteFund: (FundEntity) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    // Snackbar Messages
    val fundDeleted = stringResource(R.string.Snackbar_Fund_Deleted)

    when (popup) {
        is FundsPopup.DeleteFund -> {
            Dialog(
                onDismissRequest = onClose
            ) {
                DeletePopup(
                    title = stringResource(id = R.string.Popups_DeleteFundConfirm_Message),
                    onEndPopup = { result ->
                        when (result) {
                            PopupResult.Deleted -> {
                                onDeleteFund(popup.fund)
                                onShowSnackbar(fundDeleted)
                            }

                            PopupResult.Cancelled -> onClose()
                        }
                    }
                )
            }
        }

        FundsPopup.None -> Unit
    }
}

// ────────────────{ Screen Layout }────────────────
@Composable
private fun FundsScreenRoot(
    isHideData: Boolean,
    fundsTotalBalance: Double,
    funds: List<FundEntity>,
    onFundOptionsClick: (FundEntity) -> Unit,
    onAddAmountClick: (FundEntity) -> Unit,
    onTransferClick: (FundEntity) -> Unit,
    onDeleteFundClick: (FundEntity) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        FundsPageState(
            funds = funds,
            isHideData = isHideData,
            onFundOptionsClick = onFundOptionsClick,
            onAddAmountClick = onAddAmountClick,
            onTransferClick = onTransferClick,
            onDeleteFundClick = onDeleteFundClick
        )

        if (funds.isNotEmpty())
            TotalFundsValue(
                modifier = Modifier.align(Alignment.TopCenter),
                isHideData = isHideData,
                fundsTotalBalance = fundsTotalBalance
            )
    }
}

// ────────────────{ Components }────────────────
// ────────( Total Value )────────
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
            .height(45.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .border(
                1.dp, MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = stringResource(R.string.FundsScreen_FundsTotal),
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HideTextData(
                modifier = Modifier.weight(1f, fill = false),
                isHideData = isHideData,
                text = fundsTotalBalance.formatAmount(),
                color = MaterialTheme.colorScheme.primary,
                style = MyFontStyle.large(),
                align = Alignment.CenterEnd,
                isAmount = true
            )
            AppCurrency(textColor = MaterialTheme.colorScheme.primary)
        }
    }
}

// ────────( Funds List )────────
@Composable
private fun FundsPageState(
    isHideData: Boolean,
    funds: List<FundEntity>,
    onFundOptionsClick: (FundEntity) -> Unit,
    onAddAmountClick: (FundEntity) -> Unit,
    onTransferClick: (FundEntity) -> Unit,
    onDeleteFundClick: (FundEntity) -> Unit,
) {
    if (funds.isEmpty()) {
        EmptyPage(
            modifier = Modifier
                .fillMaxSize(),
            topText = stringResource(id = R.string.FundsScreen_EmptyFundsPage_Title),
            image = painterResource(id = R.drawable.img_empty_fund),
            imageScale = 1f,
            imageSize = 300.dp,
            bottomText = stringResource(id = R.string.FundsScreen_EmptyFundsPage_Description),
            bottomSpace = 75.dp
        )
    } else {
        FundsList(
            isHideData = isHideData,
            funds = funds,
            onFundOptionsClick = onFundOptionsClick,
            onAddAmountClick = onAddAmountClick,
            onTransferClick = onTransferClick,
            onDeleteFundClick = onDeleteFundClick
        )
    }
}

@Composable
private fun FundsList(
    isHideData: Boolean,
    funds: List<FundEntity>,

    onFundOptionsClick: (FundEntity) -> Unit,
    onAddAmountClick: (FundEntity) -> Unit,
    onTransferClick: (FundEntity) -> Unit,
    onDeleteFundClick: (FundEntity) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clip(MyRoundedCornerShape.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 70.dp, bottom = 75.dp),
    ) {
        itemsIndexed(
            items = funds,
            key = { _, fund -> fund.id },
            contentType = { _, _ -> "FundItem" }) { index, fund ->

            FundItem(
                isHideData = isHideData,
                fund = fund,
                onFundOptionsClick = onFundOptionsClick,
                onAddAmountClick = onAddAmountClick,
                onTransferClick = onTransferClick,
                onDeleteFundClick = onDeleteFundClick
            )

            if (index != funds.lastIndex) {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
private fun FundItem(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fund: FundEntity,
    onFundOptionsClick: (FundEntity) -> Unit,
    onAddAmountClick: (FundEntity) -> Unit,
    onTransferClick: (FundEntity) -> Unit,
    onDeleteFundClick: (FundEntity) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = MyRoundedCornerShape.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Fund Background
            Image(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.6f),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background),
            )

            // Excepted Line
            if (fund.isExcepted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-10).dp, y = (10).dp)
                        .width(60.dp)
                        .height(5.dp)
                        .rotate(
                            if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
                                35f
                            } else {
                                -35f
                            }
                        )
                        .background(MaterialTheme.colorScheme.error)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                FundHeader(
                    modifier = Modifier.fillMaxWidth(),
                    isHideData = isHideData,
                    fund = fund,
                    onFundOptionsClick = onFundOptionsClick
                )

                FundActions(
                    modifier = Modifier.fillMaxWidth(),
                    fund = fund,
                    onAddAmountClick = onAddAmountClick,
                    onTransferClick = onTransferClick,
                    onDeleteFundClick = onDeleteFundClick
                )
            }
        }
    }
}

//── Tini Fund Item Components ──
@Composable
private fun FundHeader(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    fund: FundEntity,
    onFundOptionsClick: (FundEntity) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(3f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcons.Settings(
                modifier = Modifier
                    .offset(y = (-2.5).dp)
                    .noRippleClickable { onFundOptionsClick(fund) },
                filledState = false,
                size = 25.dp,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.width(15.dp))

            HideTextData(
                isHideData = isHideData,
                text = (fund.name),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium(),
                align = Alignment.CenterStart
            )
        }
        Row(
            modifier = Modifier.weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            HideTextData(
                modifier = Modifier.weight(1f),
                isHideData = isHideData,
                text = fund.balance.formatAmount(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.large(),
                align = Alignment.CenterEnd,
                isAmount = true
            )
            AppCurrency(textColor = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
private fun FundActions(
    modifier: Modifier = Modifier,
    fund: FundEntity,
    onAddAmountClick: (FundEntity) -> Unit,
    onTransferClick: (FundEntity) -> Unit,
    onDeleteFundClick: (FundEntity) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.noRippleClickable { onAddAmountClick(fund) },
            text = stringResource(R.string.FundsScreen_AddBalance_Bttn),
            color = MaterialTheme.colorScheme.inversePrimary,
            style = MyFontStyle.small()
        )
        Text(
            modifier = Modifier.noRippleClickable { onTransferClick(fund) },
            text = stringResource(R.string.FundsScreen_FundsTransfer_Bttn),
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.small()
        )
        Text(
            modifier = Modifier.noRippleClickable { onDeleteFundClick(fund) },
            text = stringResource(R.string.Delete_Bttn),
            color = MaterialTheme.colorScheme.error,
            style = MyFontStyle.small()
        )
    }
}