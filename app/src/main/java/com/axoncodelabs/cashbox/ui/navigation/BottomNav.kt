package com.axoncodelabs.cashbox.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.axoncodelabs.cashbox.ui.theme.CashBoxTheme
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNav() {
    val navController = rememberNavController()

    Scaffold(bottomBar = { BottomBar(navController = navController) }) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Expenses,
        BottomBarScreen.Reports,
        BottomBarScreen.Funds,
        BottomBarScreen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column {

        HorizontalDivider(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                .clip(MyRoundedCornerShape.large),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f))
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun RowScope.AddItem(
    screen: BottomBarScreen, currentDestination: NavDestination?, navController: NavHostController,
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    val contentColor =
        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (selected) screen.iconFocused else screen.icon),
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = stringResource(screen.title),
                    color = contentColor,
                    style = MyFontStyle.medium()
                )
            }
        }
    }
}

/**--------------------[ Preview ]--------------------**/
@Preview(showBackground = true)
@Composable
private fun Preview() {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        CashBoxTheme(darkTheme = false) {
            BottomNav()
        }
    }
}