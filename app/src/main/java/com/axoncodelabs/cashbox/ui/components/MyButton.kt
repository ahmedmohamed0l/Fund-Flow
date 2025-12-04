package com.axoncodelabs.cashbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.ui.theme.MyFontStyle
import com.axoncodelabs.cashbox.ui.theme.MyRoundedCornerShape

@Composable
fun MyBotton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MyRoundedCornerShape.medium)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            style = MyFontStyle.medium(),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}