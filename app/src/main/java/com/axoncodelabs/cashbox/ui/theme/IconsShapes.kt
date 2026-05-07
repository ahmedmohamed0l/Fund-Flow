package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.axoncodelabs.cashbox.R

// Usage of this file: Ic_Name( size = 30.dp, color = MyColors.LightRed, modifier = Modifier.noRippleClickable { })
object MyIcons {

    @Composable
    fun Calendar(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        /** > **[1f] = No transparency, [0f] = Fully transparent** */
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = "Date picker",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun TransactionWallet(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_expense_wallet),
            contentDescription = "AddExpense description",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Money(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_money),
            contentDescription = "AddExpense description",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Description(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_description),
            contentDescription = "AddExpense description",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun CreditCard(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_credit_card),
            contentDescription = "Fund Selection",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Time_Zone(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search_activity),
            contentDescription = "Time Zone Selection",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Settings(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
        filledState: Boolean,
    ) {
        val filledState =
            if (filledState) painterResource(id = R.drawable.ic_settings_filled) else painterResource(
                id = R.drawable.ic_settings
            )
        Icon(
            painter = filledState,
            contentDescription = "Nav Settings",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun ThemeIcon(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_routine),
            contentDescription = "Theme icon for settings ui",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    // Theme Switch Icons
    @Composable
    fun LightTheme(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Default.LightMode,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun DarkTheme(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Default.Nightlight,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun HideData(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Outlined.VisibilityOff,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun CreateBackup(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_backup),
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun RestoreBackup(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Rounded.History,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun BackupFile(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Outlined.Save,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun DeleteBackup(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Outlined.DeleteForever,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun AutoBackup(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudSync,
            contentDescription = "",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Arrow(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
        autoMirroredState: Boolean,
    ) {
        val mirroredState =
            if (autoMirroredState) Icons.AutoMirrored.Rounded.KeyboardArrowRight else Icons.Rounded.KeyboardArrowRight
        Icon(
            imageVector = mirroredState,
            contentDescription = "Arrow icon",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color
        )
    }

    @Composable
    fun IsLatestBackupArrow(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
        autoMirroredState: Boolean,
    ) {
        val mirroredState =
            if (autoMirroredState) (angle + 180) else angle
        Icon(
            painter = painterResource(id = R.drawable.ic_line_end_arrow_notch),
            contentDescription = "Arrow icon",
            modifier = modifier
                .size(size)
                .rotate(mirroredState)
                .alpha(alphaValue),
            tint = color
        )
    }
}