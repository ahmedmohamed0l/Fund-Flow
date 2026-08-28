package com.axoncodelabs.fundflow.ui.components.sheets.popups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
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
import com.axoncodelabs.fundflow.ui.theme.MyRoundedCornerShape

@Composable
fun DeletePopup(
    title: String,
    onEndPopup: (PopupResult) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 30.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MyFontStyle.small(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.Cancel_Bttn),
                color = MaterialTheme.colorScheme.primary,
                style = MyFontStyle.small(),
                modifier = Modifier.noRippleClickable { onEndPopup(PopupResult.Cancelled) }
            )
            Text(
                text = stringResource(id = R.string.Delete_Bttn),
                color = MaterialTheme.colorScheme.error,
                style = MyFontStyle.small(),
                modifier = Modifier.noRippleClickable { onEndPopup(PopupResult.Deleted) })

        }
    }
}