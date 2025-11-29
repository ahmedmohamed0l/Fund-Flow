package com.axoncodelabs.cashbox.ui.screens.funds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar

@Composable
fun FundsScreen(viewModel: FundsViewModel = hiltViewModel()) {
    FundsScreenRoot()
}

@Composable
fun FundsScreenRoot() {
    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(id = R.string.ReportsScreen_Identifier),
                showAction = true,
                actionIcon = painterResource(id = R.drawable.ic_add_card),
                onActionClick = {}
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}

/*/**--------------------[ Preview ]--------------------**/
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(
        //LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = false
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            FundsScreenRoot()
        }
    }
}*/