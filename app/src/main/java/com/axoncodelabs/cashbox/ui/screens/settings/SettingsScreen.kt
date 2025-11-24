package com.axoncodelabs.cashbox.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val darkMode by viewModel.darkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.SettingsScreen_Identifier),
            style = MyFontStyle.extremeBold(),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))


        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ThemeSwitch(darkMode = darkMode == true, onToggle = { viewModel.setDarkMode(it) })
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                "تفعيل الوضع الداكن",
                style = MyFontStyle.medium(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}

@Composable
private fun ThemeSwitch(darkMode: Boolean, onToggle: (Boolean) -> Unit) {
    Switch(
        modifier = Modifier.scale(0.7f),
        checked = darkMode,
        onCheckedChange = { onToggle(it) }
    )
}

/**--------------------[ Preview ]--------------------**/
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(
        //LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        CashBoxTheme(
            //darkTheme = false
        ) {
            SettingsScreen()
        }
    }
}