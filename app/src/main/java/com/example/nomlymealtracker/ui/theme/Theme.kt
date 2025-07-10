package com.example.nomlymealtracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val NomlyLightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = Color.White,
    secondary = MidOrange,
    onSecondary = TextBlack,
    background = LightOrange,
    onBackground = TextBlack,
    surface = Color.White,
    onSurface = TextBlack,
    outline = Orange,
)

private val NomlyDarkColorScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Color.Black,
    secondary = DarkOrange,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    onBackground = TextWhite,
    surface = Color(0xFF1E1E1E),
    onSurface = TextWhite,
    outline = Orange,
)

@Composable
fun NomlyMealTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        darkTheme -> NomlyDarkColorScheme
        else -> NomlyLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}