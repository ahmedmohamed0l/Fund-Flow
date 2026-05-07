package com.axoncodelabs.cashbox.ui.screens.settings.components.sheets.backupOptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.cashbox.R
import com.axoncodelabs.cashbox.ui.components.MainBttn
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyIcons

@Composable
fun BackupOptionsSheet(
    viewModel: BackupOptionsVM = hiltViewModel(),
    isAutoBackup: Boolean,
    onToggleAutoBackup: (Boolean) -> Unit,
    onClose: () -> Unit,
) {
    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }

    BackupOptionsSheetRoot(
        onCreateBackup = { viewModel.onEvent(BackupOptionsEvent.CreateBackup) },
        isAutoBackup = isAutoBackup,
        onToggleAutoBackup = onToggleAutoBackup
    )
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun BackupOptionsSheetRoot(
    onCreateBackup: () -> Unit,
    isAutoBackup: Boolean,
    onToggleAutoBackup: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        CreateBackup(
            modifier = Modifier.fillMaxWidth(),
            onCreateBackup = onCreateBackup
        )
        HorizontalDivider(
            modifier = Modifier
                .clip(CircleShape)
                .padding(vertical = 15.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )

        AutoBackup(
            modifier = Modifier.fillMaxWidth(),
            isAutoBackup = isAutoBackup,
            onAutoBackupClick = onToggleAutoBackup
        )
    }
}

// ────────────────{ Components }────────────────
@Composable
private fun CreateBackup(
    modifier: Modifier = Modifier,
    onCreateBackup: () -> Unit,
) {
    Row(
        modifier = modifier,
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
        MainBttn(
            modifier = Modifier.width(80.dp),
            text = stringResource(R.string.Sheet_CreateBackupBttn),
            height = 35.dp,
            clipShape = CircleShape,
            textStyle = MyFontStyle.small(),
            onClick = onCreateBackup
        )
    }
}

@Composable
private fun AutoBackup(
    modifier: Modifier = Modifier,
    isAutoBackup: Boolean,
    onAutoBackupClick: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIcons.AutoBackup(
                modifier = Modifier.offset(y = (-2.5).dp),
                size = 25.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.Sheet_AutoBackup),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MyFontStyle.medium(),
            )
        }
        Switch(
            checked = isAutoBackup,
            onCheckedChange = { onAutoBackupClick(it) },
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