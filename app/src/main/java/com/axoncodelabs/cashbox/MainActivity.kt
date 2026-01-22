package com.axoncodelabs.cashbox

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.axoncodelabs.cashbox.ui.components.topAppBar.MyTopAppBar
import com.axoncodelabs.cashbox.ui.components.topAppBar.TopBarState
import com.axoncodelabs.cashbox.ui.navigation.BottomBar
import com.axoncodelabs.cashbox.ui.navigation.BottomNavGraph
import com.axoncodelabs.cashbox.ui.screens.settings.SettingsViewModel
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme
import com.axoncodelabs.cashbox.ui.theme.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val context = LocaleHelper.setLocale(newBase, Locale.forLanguageTag("ar"))
        super.attachBaseContext(context)
    }

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
    val viewModel: SettingsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()
    val hazeState = remember { HazeState() }
    var topBarState by remember {
        mutableStateOf(TopBarState(titleRes = R.string.ExpensesScreen_Identifier))
    }

    state.darkMode?.let { dark ->
        CashBoxTheme(darkTheme = dark) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    MyTopAppBar(state = topBarState)
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {

                    BottomNavGraph(
                        modifier = Modifier
                            .fillMaxSize()
                            .haze(state = hazeState),
                        navController = navController,
                        onTopBarChange = { topBarState = it }
                    )

                    BottomBar(
                        navController = navController,
                        hazeState = hazeState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .pointerInput(Unit) {}
                    )
                }
            }
        }
    }
}

/*/**--------------------[ Preview ]--------------------**/
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val darkMode = false
        CashBoxTheme(
            darkTheme = darkMode
        ) {
            AddFundSheetRoot(
                name = "",
                amount = "",
                description = "",
                onNameChange = {},
                onAmountChange = {},
                onDescriptionChange = {},
                onSaveClick = {}
            )
        }
    }
}*/