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

val SegoeUi = FontFamily(
    Font(R.font.segoe_ui, FontWeight.Normal),
    Font(R.font.segoe_ui_bold, FontWeight.Bold),
    Font(R.font.segoe_ui_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.segoe_ui_bold_italic, FontWeight.Bold, FontStyle.Italic)
)

val MainFont = SegoeUi

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MainFont,
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
        fontFamily = MainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    @Composable
    fun smallBold() = TextStyle(
        fontFamily = MainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )

    @Composable
    fun medium() = TextStyle(
        fontFamily = MainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    @Composable
    fun mediumBold() = TextStyle(
        fontFamily = MainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    @Composable
    fun large() = TextStyle(
        fontFamily = MainFont,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp
    )

    @Composable
    fun largeBold() = TextStyle(
        fontFamily = MainFont,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    )
}