package com.axoncodelabs.fundflow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.axoncodelabs.fundflow.R

private val Tajawal = FontFamily(
    Font(R.font.tajawal_medium, FontWeight.Normal),
    Font(R.font.tajawal_bold, FontWeight.Bold),
)

private val mainFont = Tajawal

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

// ────────────────{ Font Styles }────────────────
object MyFontStyle {

    //──── Small ────
    fun xSmall() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    fun small() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

    fun smallBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

    //──── Medium ────
    fun mediumXLight() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 16.sp
    )

    fun medium() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    fun mediumBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    //──── Large ────
    fun largeXLight() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 20.sp
    )

    fun large() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    fun largeBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    fun xLarge() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    )

    fun xLargeBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )

    fun xxLarge() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    )

    fun xxLargeBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )

    fun xxxLarge() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    )

    fun xxxLargeSimiBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp
    )
}