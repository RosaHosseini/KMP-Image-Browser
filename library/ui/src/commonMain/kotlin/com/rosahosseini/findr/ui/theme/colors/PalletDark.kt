package com.rosahosseini.findr.ui.theme.colors

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val palletDark: ColorScheme
    get() = darkColorScheme(
        primary = FindrColor.DarkBackground,
        onPrimary = FindrColor.Silver,
        secondary = FindrColor.BrandColor,
        onSecondary = Color.White,
        tertiary = FindrColor.Grey20,
        onTertiary = FindrColor.Grey90,
        primaryContainer = FindrColor.LightBackground,
        onPrimaryContainer = FindrColor.TextDark,
        secondaryContainer = FindrColor.Grey50.copy(alpha = 0.5f),
        onSecondaryContainer = FindrColor.TextDark,
        background = FindrColor.DarkBackground,
        onBackground = FindrColor.TextLight,
        error = FindrColor.Orange,
        onError = Color.White,
        surface = FindrColor.DarkBackground,
        onSurface = FindrColor.TextLight
    )
