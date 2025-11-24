package com.axoncodelabs.cashbox.ui.screens.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

@Composable
fun ExpensesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.ExpensesScreen_Identifier),
            style = MyFontStyle.extremeBold(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}