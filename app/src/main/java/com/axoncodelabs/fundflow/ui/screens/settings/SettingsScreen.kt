package com.axoncodelabs.fundflow.ui.screens.settings

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.BuildConfig
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.util.backup.BackupInfo
import com.axoncodelabs.fundflow.ui.components.appTopBar.AppTopBarState
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.backupOptions.BackupOptionsSheet
import com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.backupSelection.BackupSelectionSheet
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons
import com.axoncodelabs.fundflow.ui.theme.MyRoundedCornerShape
import com.axoncodelabs.fundflow.ui.util.backupDateFormater

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onTopBarChange: (AppTopBarState) -> Unit,
) {
    //──── State & ViewModel Setup ────
    val state by viewModel.state.collectAsState()
    val sheet = state.currentSheet

    //──── AppTopBar Data ────
    SideEffect {
        onTopBarChange(AppTopBarState(titleRes = R.string.SettingsScreen_Identifier))
    }

    //──── Sheets & Popups Handling ────
    SheetsHandler(
        sheet = sheet,
        isAutoBackup = state.isAutoBackup ?: false,
        onToggleAutoBackup = { viewModel.onEvent(SettingsEvent.ToggleAutoBackup(it)) },

        backupList = state.backupList,
        refreshBackups = { viewModel.refreshBackups() },
        onClose = {
            viewModel.onEvent(SettingsEvent.CloseSheet)
            viewModel.refreshBackups()
        }
    )

    //──── Screen Layout ────
    SettingsScreenRoot(
        darkMode = state.darkMode == true,
        onThemeSwitcherClick = { viewModel.onEvent(SettingsEvent.ToggleTheme(it)) },

        isHideData = state.isHideData,
        onHideDataClick = { viewModel.onEvent(SettingsEvent.ToggleHideData(it)) },

        lastBackupDate = state.lastBackupDate?.backupDateFormater(),
        onShowBackupSheet = {
            viewModel.onEvent(SettingsEvent.SheetDisplayed(SettingsSheets.BackupOptions(state.isAutoBackup == false)))
        },

        onShowRestoreSheet = { viewModel.onEvent(SettingsEvent.SheetDisplayed(SettingsSheets.BackupSelection)) },
    )
}

// ────────────────{ Handlers }────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetsHandler(
    sheet: SettingsSheets,
    isAutoBackup: Boolean,
    onToggleAutoBackup: (Boolean) -> Unit,

    backupList: List<BackupInfo>,
    refreshBackups: () -> Unit,
    onClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (sheet != SettingsSheets.None) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            containerColor = MaterialTheme.colorScheme.background,
            sheetState = sheetState
        ) {
            when (sheet) {
                is SettingsSheets.BackupOptions -> {
                    BackupOptionsSheet(
                        isAutoBackup = isAutoBackup,
                        onToggleAutoBackup = onToggleAutoBackup,
                        onClose = {
                            onClose()
                            refreshBackups()
                        }
                    )
                }

                SettingsSheets.BackupSelection -> {
                    BackupSelectionSheet(
                        backups = backupList,
                        refreshBackups = refreshBackups,
                        onClose = {
                            onClose()
                        }
                    )
                }

                SettingsSheets.None -> Unit
            }
        }
    }
}

// ────────────────{ Screen Layout }────────────────
@Composable
private fun SettingsScreenRoot(
    darkMode: Boolean,
    onThemeSwitcherClick: (Boolean) -> Unit,

    isHideData: Boolean,
    onHideDataClick: (Boolean) -> Unit,

    lastBackupDate: String?,
    onShowBackupSheet: () -> Unit,

    onShowRestoreSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
            .padding(bottom = 75.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ThemeToggle(
            modifier = Modifier.fillMaxWidth(),
            darkMode = darkMode,
            onThemeSwitcherClick = onThemeSwitcherClick
        )
        RowsDivider()

        HideData(
            modifier = Modifier.fillMaxWidth(),
            isHideData = isHideData,
            onHideDataClick = onHideDataClick
        )
        RowsDivider()

        BackupRow(
            modifier = Modifier.fillMaxWidth(),
            lastBackupDate = lastBackupDate,
            onBackupClick = onShowBackupSheet
        )
        RowsDivider()

        RestoreRow(
            modifier = Modifier.fillMaxWidth(),
            onRestoreClick = onShowRestoreSheet
        )
        RowsDivider()

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "v${BuildConfig.VERSION_NAME}",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MyFontStyle.medium()
        )
    }
}

//── Divider ──
@Composable
private fun RowsDivider() {
    HorizontalDivider(
        modifier = Modifier.clip(MyRoundedCornerShape.large),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSecondary
    )
}

// ────────────────{ Components }────────────────
// ────────( Switch Theme )────────
@Composable
private fun ThemeToggle(
    modifier: Modifier = Modifier,
    darkMode: Boolean,
    onThemeSwitcherClick: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 20.dp),
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

        ThemeSwitch(
            darkMode = darkMode,
            onThemeSwitcherClick = onThemeSwitcherClick
        )
    }
}

//── Tini Components ──
@Composable
private fun ThemeSwitch(
    darkMode: Boolean,
    size: Dp = 35.dp,
    onThemeSwitcherClick: (Boolean) -> Unit,
    color1: Color = MaterialTheme.colorScheme.primaryContainer,
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
                .offset { IntOffset(offset.roundToPx(), 0) }
                .padding(all = 3.dp)
                .clip(shape = CircleShape)
                .background(color2)
        ) {}
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                MyIcons.DarkTheme(
                    size = (size / 3),
                    color = if (darkMode) color1 else color2
                )
            }

            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                MyIcons.LightTheme(
                    size = (size / 3),
                    color = if (darkMode) color2 else color1
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
        modifier = modifier.padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.HideData(
                modifier = Modifier.offset(y = (-1.5).dp),
                size = 25.dp,
                color = MaterialTheme.colorScheme.onBackground
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
            modifier = Modifier
                .size(width = 55.dp, height = 30.dp)
                .scale(1f)
                .offset(y = (-0.3).dp),
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

// ────────( Backup )────────
@Composable
private fun BackupRow(
    modifier: Modifier = Modifier,
    lastBackupDate: String?,
    onBackupClick: () -> Unit
) {
    Row(
        modifier = modifier
            .noRippleClickable { onBackupClick() }
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.CreateBackup(
                modifier = Modifier.offset(y = (-2.5).dp),
                size = 25.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.SettingsScreen_Backup),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.medium()
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!lastBackupDate.isNullOrEmpty()) {
                Text(
                    text = lastBackupDate,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = MyFontStyle.xSmall()
                )
            }
            MyIcons.Arrow(
                modifier = Modifier.offset(y = (-2.5).dp),
                autoMirroredState = true,
                size = 30.dp,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

// ────────( Restore )────────
@Composable
private fun RestoreRow(
    modifier: Modifier = Modifier,
    onRestoreClick: () -> Unit
) {
    Row(
        modifier = modifier
            .noRippleClickable { onRestoreClick() }
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.RestoreBackup(
                modifier = Modifier.offset(y = (-2.5).dp),
                size = 25.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.SettingsScreen_Restore),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.medium()
            )
        }
        MyIcons.Arrow(
            modifier = Modifier.offset(y = (-2.5).dp),
            autoMirroredState = true,
            size = 30.dp,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}