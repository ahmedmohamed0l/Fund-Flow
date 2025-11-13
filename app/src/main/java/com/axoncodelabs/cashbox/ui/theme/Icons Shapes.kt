package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.axoncodelabs.cashbox.R

/*
* Usage of this file:
* Ic_Name(
                size = dp(30),
                color = MyColors.LightRed,
                modifier = Modifier.clickable { })
* */

object MyIcons {

    @Composable
    fun AddExpense_Description(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        /** [1f] = No transparency, [0f] = Fully transparent **/
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_addexpense_description),
            contentDescription = "AddExpense Description",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Arrow(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        /** [1f] = No transparency, [0f] = Fully transparent **/
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "Arrow Icon",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Expenses_AddBtn(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_expenses_addbtn),
            contentDescription = "Float Add Button",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Funds_AddFund(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_funds_add_fund),
            contentDescription = "Add Fund",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Funds_AddMoney(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_funds_add_money),
            contentDescription = "Add Money",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Funds_DeleteFund(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_funds_delete_fund),
            contentDescription = "Delete Fund",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Funds_TimeZone(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_funds_timezone),
            contentDescription = "Time Zone Selection",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Funds_Transfer(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_funds_transfer),
            contentDescription = "Add Money",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Expenses(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_expenses),
            contentDescription = "Nav Funds",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Expenses_Focused(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_expenses_focused),
            contentDescription = "Nav Funds",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Funds(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_funds),
            contentDescription = "Nav Funds",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Funds_Focused(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_funds_focused),
            contentDescription = "Nav Funds Focused",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Settings(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_settings),
            contentDescription = "Settings Photo Focused",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Settings_Focused(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_settings_focused),
            contentDescription = "Settings Photo Focused",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Reports(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_reports),
            contentDescription = "Settings Photo Focused",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }

    @Composable
    fun Nav_Reports_Focused(
        modifier: Modifier = Modifier,
        size: Dp = dp(48),
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nav_reports_focused),
            contentDescription = "Settings Photo Focused",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            colorFilter = ColorFilter.tint(color)
        )
    }
}
