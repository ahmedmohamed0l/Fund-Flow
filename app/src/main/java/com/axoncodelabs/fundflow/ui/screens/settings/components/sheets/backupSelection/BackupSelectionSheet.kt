package com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.backupSelection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.data.util.backup.BackupInfo
import com.axoncodelabs.fundflow.ui.components.CustomSnackbar
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.ui.theme.MyIcons
import com.axoncodelabs.fundflow.ui.util.backupDateFormater
import kotlinx.coroutines.delay


@Composable
fun BackupSelectionSheet(
    viewModel: BackupSelectionVM = hiltViewModel(),
    backups: List<BackupInfo>,
    refreshBackups: () -> Unit,
    onClose: () -> Unit,
) {
    // Send Close Event
    LaunchedEffect(Unit) {
        viewModel.closeEvent.collect {
            onClose()
        }
    }

    //──── Custom Snackbar Handler ────
    var isShowSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    LaunchedEffect(isShowSnackbar) {
        if (isShowSnackbar) {
            delay(2500)
            isShowSnackbar = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (backups.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.Sheet_EmptyBackupList1),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MyFontStyle.large(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.Sheet_EmptyBackupList2),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MyFontStyle.medium(),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val backupDeletedMsg = stringResource(R.string.SettingsScreen_Snackbar_BackupDeleted)
            BackupSelectionSheetRoot(
                backups = backups,
                onSelect = { fileName ->
                    viewModel.onEvent(BackupSelectionEvent.OnSelectBackup(fileName))
                },
                onDelete = { fileName ->
                    viewModel.onEvent(BackupSelectionEvent.OnDeleteBackup(fileName))
                    refreshBackups()
                    snackbarMessage = backupDeletedMsg
                    isShowSnackbar = true
                }
            )
        }

        AnimatedVisibility(
            visible = isShowSnackbar,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            CustomSnackbar(message = snackbarMessage)
        }
    }
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun BackupSelectionSheetRoot(
    backups: List<BackupInfo>,
    onSelect: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(R.string.Sheet_ChoseBackup),
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.largeBold()
        )
        Spacer(Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(backups) { index, backup ->
                val isLatest = index == 0
                BackupItem(
                    backup = backup,
                    isLatest = isLatest,
                    onSelect = { onSelect(backup.folderName) },
                    onDelete = { onDelete(backup.folderName) },
                )
                if (index != backups.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.clip(CircleShape),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// ────────────────{ Components }────────────────
@Composable
private fun BackupItem(
    modifier: Modifier = Modifier,
    isLatest: Boolean = false,
    backup: BackupInfo,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { onSelect() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcons.BackupFile(
                modifier = Modifier.offset(y = (-2.5).dp),
                size = 25.dp,
                color = if (isLatest) MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline,
            )
            Spacer(Modifier.width(5.dp))

            Text(
                text = backup.dateTimeMillis.backupDateFormater(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MyFontStyle.medium()
            )

            if (isLatest) {
                MyIcons.IsLatestBackupArrow(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .offset(y = (-1).dp),
                    size = 15.dp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    autoMirroredState = true
                )
                Text(
                    text = "( ${stringResource(R.string.Sheet_ItsLastBackup)} )",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = MyFontStyle.small()
                )
            }
        }

        MyIcons.DeleteBackup(
            modifier = Modifier
                .offset(y = (-2.5).dp)
                .noRippleClickable { onDelete() },
            size = 25.dp,
            color = MaterialTheme.colorScheme.error,
        )
    }
}