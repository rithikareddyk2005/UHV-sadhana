package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = UhvGoldAccent,
    onPrimary = UhvNavyDark,
    primaryContainer = UhvNavyPrimary,
    onPrimaryContainer = UhvGoldLight,
    secondary = UhvGoldAccent,
    onSecondary = UhvNavyDark,
    secondaryContainer = UhvSurfaceVariantDark,
    onSecondaryContainer = UhvGoldLight,
    tertiary = UhvIvory,
    background = UhvBackgroundDark,
    onBackground = UhvTextPrimaryLight,
    surface = UhvSurfaceDark,
    onSurface = UhvTextPrimaryLight,
    surfaceVariant = UhvSurfaceVariantDark,
    onSurfaceVariant = UhvTextSecondaryLight,
    error = UhvError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = UhvNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = UhvSurfaceVariantLight,
    onPrimaryContainer = UhvNavyPrimary,
    secondary = UhvGoldAccent,
    onSecondary = UhvNavyDark,
    secondaryContainer = UhvGoldLight,
    onSecondaryContainer = UhvNavyPrimary,
    tertiary = UhvNavyPrimary,
    background = UhvBackgroundLight,
    onBackground = UhvTextPrimaryDark,
    surface = UhvSurfaceLight,
    onSurface = UhvTextPrimaryDark,
    surfaceVariant = UhvSurfaceVariantLight,
    onSurfaceVariant = UhvTextSecondaryDark,
    error = UhvError,
    onError = Color.White
)

@Composable
fun UhvSadhanaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
