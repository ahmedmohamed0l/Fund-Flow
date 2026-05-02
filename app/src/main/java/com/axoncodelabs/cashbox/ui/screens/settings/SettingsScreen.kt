package com.axoncodelabs.cashbox.ui.screens.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.cashbox.ui.components.noRippleClickable
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onTopBarChange: (AppTopBarState) -> Unit,
) {
    //──── State & ViewModel Setup ────
    val state by viewModel.state.collectAsState()

    //──── AppTopBar Data ────
    SideEffect {
        onTopBarChange(AppTopBarState(titleRes = R.string.SettingsScreen_Identifier))
    }

    //──── Screen Layout ────
    SettingsScreenRoot(
        darkMode = state.darkMode == true,
        onThemeSwitcherClick = { viewModel.onEvent(SettingsEvent.ToggleTheme(it)) },
        isHideData = state.isHideData,
        onHideDataClick = { viewModel.onEvent(SettingsEvent.ToggleHideData(it)) }
    )
}

// ────────────────{ Screen Layout }────────────────
@Composable
private fun SettingsScreenRoot(
    darkMode: Boolean,
    onThemeSwitcherClick: (Boolean) -> Unit,
    isHideData: Boolean,
    onHideDataClick: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(top = 10.dp, bottom = 29.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SwitchTheme(
            modifier = Modifier.fillMaxWidth(),
            darkMode = darkMode,
            onThemeSwitcherClick = onThemeSwitcherClick
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .clip(MyRoundedCornerShape.large),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        HideData(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            onHideDataClick = onHideDataClick
        )

    }
}

// ────────────────{ Components }────────────────
// ────────( Switch Theme )────────
@Composable
private fun SwitchTheme(
    modifier: Modifier = Modifier,
    darkMode: Boolean,
    onThemeSwitcherClick: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.ThemeIcon(
                modifier = Modifier.offset(y = (-2.5).dp),
                size = 25.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.SettingsScreen_ChangeTheme),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.medium(),
            )
        }

        ThemeSwitcher(
            darkMode = darkMode,
            onThemeSwitcherClick = onThemeSwitcherClick
        )
    }
}

//── Tini Components ──
@Composable
private fun ThemeSwitcher(
    darkMode: Boolean,
    size: Dp = 35.dp,
    onThemeSwitcherClick: (Boolean) -> Unit,
    color1: Color = MaterialTheme.colorScheme.primary,
    color2: Color = MaterialTheme.colorScheme.background,
) {
    val offset by animateDpAsState(
        targetValue = if (darkMode) 0.dp else size,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .width(size * 2)
            .height(size)
            .clip(shape = CircleShape)
            .noRippleClickable { onThemeSwitcherClick(!darkMode) }
            .background(color1)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = 3.dp)
                .clip(shape = CircleShape)
                .background(color2)
        ) {}
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(size / 3),
                    imageVector = Icons.Default.Nightlight,
                    contentDescription = null,
                    tint = if (darkMode) color1 else color2
                )
            }

            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(size / 3),
                    imageVector = Icons.Default.LightMode,
                    contentDescription = null,
                    tint = if (darkMode) color2 else color1
                )
            }
        }
    }
}

// ────────( Hide Data )────────
@Composable
private fun HideData(
    modifier: Modifier = Modifier,
    isHideData: Boolean,
    onHideDataClick: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.VisibilityOff,
                contentDescription = "",
                modifier = Modifier.size(25.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.SettingsScreen_HideData),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.medium(),
            )
        }
        Switch(
            checked = isHideData,
            onCheckedChange = { onHideDataClick(it) },
            modifier = Modifier.width(70.dp),
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
            )
        )
    }
}