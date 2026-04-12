package com.axoncodelabs.cashbox.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axoncodelabs.cashbox.R

/*private val SegoeUi = FontFamily(
    Font(R.font.segoe_ui, FontWeight.Normal),
    Font(R.font.segoe_ui_bold, FontWeight.Bold),
    Font(R.font.segoe_ui_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.segoe_ui_bold_italic, FontWeight.Bold, FontStyle.Italic)
)*/
private val Tajawal = FontFamily(
    Font(R.font.tajawal_medium, FontWeight.Normal),
    Font(R.font.tajawal_bold, FontWeight.Bold),
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
)

//-------------------[Font Styles]-------------------
@Composable
fun AppCurrency(
    modifier: Modifier = Modifier,
    style: TextStyle = MyFontStyle.xSmall(),
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    startPadding: Dp = 5.dp
) {
    Text(
        modifier = modifier.padding(start = startPadding),
        text = stringResource(R.string.App_Currency),
        style = style,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

object MyFontStyle {

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