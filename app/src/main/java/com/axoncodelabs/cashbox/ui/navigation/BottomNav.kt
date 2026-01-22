package com.axoncodelabs.cashbox.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild

@Composable
fun BottomBar(
    navController: NavHostController,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        BottomBarScreen.Expenses,
        BottomBarScreen.Reports,
        BottomBarScreen.Funds,
        BottomBarScreen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .shadow(
                elevation = (3.5).dp,
                shape = CircleShape,
                clip = true,
                ambientColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                spotColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
            )
            .clip(CircleShape)
            .hazeChild(
                state = hazeState,
                shape = CircleShape,
                style = HazeStyle(
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    blurRadius = 5.dp,
                    noiseFactor = 0.1f
                )
            )
            .border(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                width = 1.dp,
                shape = CircleShape
            )
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

@Composable
private fun RowScope.AddItem(
    screen: BottomBarScreen, currentDestination: NavDestination?, navController: NavHostController,
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    val contentColor =
        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
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