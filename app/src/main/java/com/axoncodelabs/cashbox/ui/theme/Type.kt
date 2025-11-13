package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
//--------------------[Modifiers]--------------------

@Composable
fun sp(x: Int) = with(LocalDensity.current) { x.sp }

@Composable
fun dp(x: Int) = with(LocalDensity.current) { x.dp }

@Composable
fun getText(text: Int) = LocalContext.current.getString(text)

//-------------------[Font Styles]-------------------

object MyFontStyle {

    @Composable
    fun small() = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

    @Composable
    fun smallBold() = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

    @Composable
    fun normal() = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    @Composable
    fun normalBold() = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    @Composable
    fun large() = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    @Composable
    fun largeBold() = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}