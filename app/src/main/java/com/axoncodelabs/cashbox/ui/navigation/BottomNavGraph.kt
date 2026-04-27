package com.axoncodelabs.cashbox.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.axoncodelabs.cashbox.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.cashbox.ui.screens.expenses.ExpensesScreen
import com.axoncodelabs.cashbox.ui.screens.funds.FundsScreen
import com.axoncodelabs.cashbox.ui.screens.reports.ReportsScreen
import com.axoncodelabs.cashbox.ui.screens.settings.SettingsScreen

@Composable
fun BottomNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onTopBarChange: (AppTopBarState) -> Unit
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = BottomBarScreen.Expenses.route,
        // To disable it, use = { EnterTransition.None }
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
    ) {
        composable(route = BottomBarScreen.Expenses.route) {
            ExpensesScreen(onTopBarChange = onTopBarChange)
        }
        composable(route = BottomBarScreen.Reports.route) {
            ReportsScreen(onTopBarChange = onTopBarChange)
        }
        composable(route = BottomBarScreen.Funds.route) {
            FundsScreen(onTopBarChange = onTopBarChange)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(onTopBarChange = onTopBarChange)
        }
    }
}