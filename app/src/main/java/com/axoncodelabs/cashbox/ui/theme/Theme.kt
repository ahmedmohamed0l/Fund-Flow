package com.axoncodelabs.cashbox.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, locale: Locale): Context {
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}

sealed class Theme(val value: String) {
    object Light : Theme("light")
    object Dark : Theme("dark")
}

private val LightColorScheme = lightColorScheme(
    background = MyColors.WhiteSmoke,
    onBackground = MyColors.Black,
    onPrimaryFixed = MyColors.White,

    primary = MyColors.DarkSkyBlue,
    onPrimary = MyColors.White,

    secondary = MyColors.WhiteSmoke,
    onSecondary = MyColors.Gray,

    surface = MyColors.SoftBlack,
    onSurface = MyColors.WhiteSmoke,

    tertiary = MyColors.LightGray,
    onTertiary = MyColors.SoftBlack,

    error = MyColors.LightRed,
    inversePrimary = MyColors.DarkGreen,

    outline = MyColors.MidLightGray

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
    onPrimaryFixed = MyColors.Black,

    primary = MyColors.DeepBlue,
    onPrimary = MyColors.White,

    secondary = MyColors.SoftBlack,
    onSecondary = MyColors.WhiteSmoke,

    surface = MyColors.WhiteSmoke,
    onSurface = MyColors.SoftBlack,

    tertiary = MyColors.LightBlack,
    onTertiary = MyColors.WhiteSmoke,

    error = MyColors.MidRed,
    inversePrimary = MyColors.MidGreen,

    outline = MyColors.Gray
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
    val activity = LocalActivity.current as Activity
    SideEffect {
        val window = activity.window
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)

        insetsController.isAppearanceLightStatusBars = !darkTheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = MyRoundedCornerShape,
        content = content
    )
}