package com.example.cookmate.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class ThemeColors(
    val background: Color,
    val onBackground: Color,
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val outline: Color
)

data class ThemeTypography(
    val screenHeading: TextStyle,
    val cardTitle: TextStyle,
    val cardSubtitle: TextStyle,
    val title: TextStyle,
    val subtitle: TextStyle,
    val buttonText: TextStyle
)

object CustomTheme {
    val themeColors: ThemeColors
    @Composable get() = LocalThemeColors.current

    val themeTypography: ThemeTypography
    @Composable get() = LocalThemeTypography.current
}

enum class ThemePaletteColors { GREEN, PURPLE, PINK }

enum class ThemeSizes { SMALL, MEDIUM, BIG }

val LocalThemeColors = staticCompositionLocalOf<ThemeColors> { error("No colors provided") }
val LocalThemeTypography = staticCompositionLocalOf<ThemeTypography> { error("No typography provided") }
