package com.axoncodelabs.fundflow.ui.screens.settings.components.sheets.language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axoncodelabs.fundflow.R
import com.axoncodelabs.fundflow.ui.components.noRippleClickable
import com.axoncodelabs.fundflow.ui.theme.MyFontStyle
import com.axoncodelabs.fundflow.util.language.Language

@Composable
fun LanguageChangerSheet(
    currentLanguage: Language,
    onSelect: (Language) -> Unit,
) {
    LanguageChangerSheetRoot(
        currentLanguage = currentLanguage,
        onSelect = onSelect
    )
}

// ────────────────{ Sheet Layout }────────────────
@Composable
private fun LanguageChangerSheetRoot(
    currentLanguage: Language,
    onSelect: (Language) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(R.string.Sheet_LanguageChanger_Label),
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.largeBold()
        )
        Spacer(Modifier.height(15.dp))

        LanguageItem(
            modifier = Modifier.fillMaxWidth(),
            language = stringResource(R.string.Sheet_LanguageChanger_Ar),
            isCurrent = currentLanguage == Language.Arabic,
            onSelect = { onSelect(Language.Arabic) }
        )

        HorizontalDivider(
            modifier = Modifier
                .clip(CircleShape)
                .padding(10.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )

        LanguageItem(
            modifier = Modifier.fillMaxWidth(),
            language = stringResource(R.string.Sheet_LanguageChanger_En),
            isCurrent = currentLanguage == Language.English,
            onSelect = { onSelect(Language.English) }
        )

    }
}

// ────────────────{ Components }────────────────
@Composable
private fun LanguageItem(
    modifier: Modifier = Modifier,
    language: String,
    isCurrent: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .noRippleClickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.offset(y = (-2.5).dp),
            selected = isCurrent,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = language,
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.medium()
        )
    }
}