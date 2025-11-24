package com.axoncodelabs.cashbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.ui.navigation.BottomNav
import com.axoncodelabs.cashbox.ui.screens.settings.SettingsViewModel
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme
import com.axoncodelabs.cashbox.ui.theme.MyColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Root()

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Root() {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val darkMode by viewModel.darkMode.collectAsState()
        if (darkMode == null) {
            // شاشة انتظار بسيطة لغاية ما DataStore يرد
            Box(modifier = Modifier.fillMaxSize().background(MyColors.Black))
        } else {
            CashBoxTheme (darkTheme = darkMode == true){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        BottomNav()
                    }
                }
            }
        }
    }
}