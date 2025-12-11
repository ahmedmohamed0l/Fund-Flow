package com.axoncodelabs.cashbox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.axoncodelabs.cashbox.ui.screens.expenses.ExpensesScreen
import com.axoncodelabs.cashbox.ui.screens.funds.FundsScreen
import com.axoncodelabs.cashbox.ui.screens.reports.ReportsScreen
import com.axoncodelabs.cashbox.ui.screens.settings.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = BottomBarScreen.Expenses.route
    ) {
        composable(route = BottomBarScreen.Expenses.route) {
            ExpensesScreen()
        }
        composable(route = BottomBarScreen.Reports.route) {
            ReportsScreen()
        }
        composable(route = BottomBarScreen.Funds.route) {
            FundsScreen()
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }
    }
}