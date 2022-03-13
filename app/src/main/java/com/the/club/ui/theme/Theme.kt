package com.the.club.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    background = black,
    surface = black,
    onPrimary = black,
    onSecondary = white,
    onBackground = white,
    onSurface = white,
)

private val LightColorPalette = lightColors(
    background = white,
    surface = white,
    onPrimary = white,
    onSecondary = black,
    onBackground = black,
    onSurface = black,
)

@Composable
fun TheClubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun textColor() : Color = if (isSystemInDarkTheme()) white else black

@Composable
fun backgroundColor() : Color = if (isSystemInDarkTheme()) black else white

@Composable
fun cardBackgroundColor() : Color = if (isSystemInDarkTheme()) darkYellow else lightYellow
