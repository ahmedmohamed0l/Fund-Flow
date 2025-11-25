package com.axoncodelabs.cashbox.ui.navigation

import com.axoncodelabs.cashbox.R

sealed class BottomBarScreen(
    val route: String, val title: Int, val icon: Int, val iconFocused: Int,
) {
    object Expenses : BottomBarScreen(
        route = "expenses",
        title = R.string.ExpensesScreen_Identifier,
        icon = R.drawable.ic_home_app_logo,
        iconFocused = R.drawable.ic_home_app_logo_filled
    )

    object Reports : BottomBarScreen(
        route = "reports",
        title = R.string.ReportsScreen_Identifier,
        icon = R.drawable.ic_analytics,
        iconFocused = R.drawable.ic_analytics_filled
    )

    object Funds : BottomBarScreen(
        route = "funds",
        title = R.string.FundsScreen_Identifier,
        icon = R.drawable.ic_account_balance_wallet,
        iconFocused = R.drawable.ic_account_balance_wallet_filled
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = R.string.SettingsScreen_Identifier,
        icon = R.drawable.ic_settings,
        iconFocused = R.drawable.ic_settings_filled
    )
}