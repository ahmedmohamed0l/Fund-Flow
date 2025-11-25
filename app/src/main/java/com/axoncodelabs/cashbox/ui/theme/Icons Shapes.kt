package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCard
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
                size = dp(30),
                color = MyColors.LightRed,
                modifier = Modifier.clickable { })
*/

/*Just for preview icons edits quikly:-
@Composable
@Preview(showBackground = true)
fun PreviewIcon() {
    Column(
        modifier = Modifier
            //.fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            MyIcons.Analytics(size = 200.dp,
                filledState = false
            )
        //}
    }
}*/

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
    fun Comment(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_comment),
            contentDescription = "AddExpense description",
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
    fun Plus(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Float add button",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Add_Card(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
    ) {
        Icon(
            imageVector = Icons.Rounded.AddCard,
            contentDescription = "Add new fund",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Search_Activity(
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
    fun Home_App_Logo(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
        filledState: Boolean,
    ) {
        val filledState =
            if (filledState) painterResource(id = R.drawable.ic_home_app_logo_filled) else painterResource(
                id = R.drawable.ic_home_app_logo
            )
        Icon(
            painter = filledState,
            contentDescription = "Nav Expenses",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }

    @Composable
    fun Account_Balance_Wallet(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
        filledState: Boolean,
    ) {
        val filledState =
            if (filledState) painterResource(id = R.drawable.ic_account_balance_wallet_filled) else painterResource(
                id = R.drawable.ic_account_balance_wallet
            )
        Icon(
            painter = filledState,
            contentDescription = "Nav Funds",
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
    fun Analytics(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp,
        angle: Float = 0f,
        color: Color = MyColors.Black,
        alphaValue: Float = 1f,
        filledState: Boolean,
    ) {
        val filledState =
            if (filledState) painterResource(id = R.drawable.ic_analytics_filled) else painterResource(
                id = R.drawable.ic_analytics
            )
        Icon(
            painter = filledState,
            contentDescription = "Nav Reports",
            modifier = modifier
                .size(size)
                .rotate(angle)
                .alpha(alphaValue),
            tint = color,
        )
    }
}
