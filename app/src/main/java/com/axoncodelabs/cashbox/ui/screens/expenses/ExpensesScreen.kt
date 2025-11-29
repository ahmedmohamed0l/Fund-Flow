package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.MyTopAppBar

@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    ExpensesScreenRoot()
}

@Composable
private fun ExpensesScreenRoot() {
    Scaffold(
        topBar = {
            MyTopAppBar(title = stringResource(id = R.string.ExpensesScreen_Identifier))
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