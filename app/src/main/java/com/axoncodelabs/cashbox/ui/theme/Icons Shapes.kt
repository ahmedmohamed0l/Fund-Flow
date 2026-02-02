package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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

/*
Usage of this file:
Ic_Name(
                size = 30.dp,
                color = MyColors.LightRed,
                modifier = Modifier.clickable { })
*/

object MyIcons {

    @Composable
    fun Calendar(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        /** [1f] = No transparency, [0f] = Fully transparent **/
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
    fun ExpenseWallet(
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
}
