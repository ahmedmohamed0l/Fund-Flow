package com.axoncodelabs.cashbox.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
    onBackground = MyColors.SoftBlack,
//    onPrimaryFixed = MyColors.White,


    primary = MyColors.DarkSkyBlue,
    onPrimary = MyColors.White,

    secondary = MyColors.LightGray,
    onSecondary = MyColors.Gray,

    surface = MyColors.White,
    onSurface = MyColors.Gray,

//    tertiary = MyColors.LightGray,

//    onTertiary = MyColors.SoftBlack,

    error = MyColors.LightRed,
    inversePrimary = MyColors.DarkGreen,

    outline = MyColors.MidLightGray,

    surfaceContainerHigh = MyColors.Orange,

    //Nav Shadow
    scrim = MyColors.Black,
    //Nav Outline
    outlineVariant = MyColors.White,
)

private val DarkColorScheme = darkColorScheme(
    background = MyColors.SoftBlack,
    onBackground = MyColors.WhiteSmoke,
//    onPrimaryFixed = MyColors.Black,


    primary = MyColors.DeepBlue,
    onPrimary = MyColors.White,

    secondary = MyColors.LightBlack,
    onSecondary = MyColors.LightGray2,

    surface = MyColors.LightBlack,
    onSurface = MyColors.SoftBlack,

//    tertiary = MyColors.LightBlack,

//    onTertiary = MyColors.WhiteSmoke,

    error = MyColors.MidRed,
    inversePrimary = MyColors.MidGreen,

    outline = MyColors.Gray,

    surfaceContainerHigh = MyColors.Orange,

    //Nav Shadow
    scrim = MyColors.Gray,
    //Nav Outline
    outlineVariant = MyColors.DarkGray,
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
    /*val activity = LocalActivity.current as Activity
    SideEffect {
        val window = activity.window
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !darkTheme
            isAppearanceLightNavigationBars = !darkTheme
        }
    }*/
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = MyRoundedCornerShape,
        content = content
    )
}