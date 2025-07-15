package com.example.nomlymealtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Current LightColorScheme for the App **/
private val NomlyLightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = Color.White,
    secondary = MidOrange,
    onSecondary = TextBlack,
    background = BackOrange,
    onBackground = TextBlack,
    surface = Color.White,
    onSurface = TextBlack,
    outline = Orange,
)

/** Current DarkColorScheme for the App **/
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

/**
 * The Nomly app theme that applies color schemes, typography, and system dark mode support.
 * @param darkTheme Whether to use the dark color scheme. Defaults to system setting.
 * @param content The composable content to display within the themed context.
 */
@Composable
fun NomlyMealTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> NomlyDarkColorScheme
        else -> NomlyLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}