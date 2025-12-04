package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.axoncodelabs.cashbox.R

private val SegoeUi = FontFamily(
    Font(R.font.segoe_ui, FontWeight.Normal),
    Font(R.font.segoe_ui_bold, FontWeight.Bold),
    Font(R.font.segoe_ui_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.segoe_ui_bold_italic, FontWeight.Bold, FontStyle.Italic)
)
private val Tajawal = FontFamily(
    Font(R.font.tajawal_medium, FontWeight.Normal),
    Font(R.font.tajawal_bold, FontWeight.Bold),
    Font(R.font.segoe_ui_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.segoe_ui_bold_italic, FontWeight.Bold, FontStyle.Italic)
)

private val mainFont = Tajawal

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = mainFont,
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

//-------------------[Font Styles]-------------------

object MyFontStyle {

    @Composable
    fun small() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

    @Composable
    fun smallBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

    @Composable
    fun medium() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    @Composable
    fun mediumBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    @Composable
    fun large() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    @Composable
    fun largeBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    @Composable
    fun extremeBold() = TextStyle(
        fontFamily = mainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 50.sp
    )
}