package com.axoncodelabs.cashbox.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class Theme(val value: String) {
    object Light : Theme("light")
    object Dark : Theme("dark")
}

private val LightColorScheme = lightColorScheme(
    background = MyColors.White,
    onBackground = MyColors.Black,

    primary = MyColors.DarkSkyBlue,
    onPrimary = MyColors.White,

    secondary = MyColors.LightGray,
    onSecondary = MyColors.White,

    tertiary = MyColors.MidGreen,

    surface = MyColors.SoftBlack,
    onSurface = MyColors.LightGray

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val DarkColorScheme = darkColorScheme(
    background = MyColors.SoftBlack,
    onBackground = MyColors.White,

    primary = MyColors.DeepBlue,
    onPrimary = MyColors.White,

    secondary = MyColors.LightBlack,
    onSecondary = MyColors.White,

    tertiary = MyColors.DarkGreen,

    surface = MyColors.LightGray,
    onSurface = MyColors.SoftBlack
)

@Composable
fun CashBoxTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = MyRoundedCornerShape,
        content = content
    )
}