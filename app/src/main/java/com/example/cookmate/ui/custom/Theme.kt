package com.example.cookmate.ui.custom

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.cookmate.ui.custom.ThemePaletteColors.*
import com.example.cookmate.ui.custom.ThemeSizes.*

@Composable
fun Theme(
    colorPalette: ThemePaletteColors = GREEN,
    fontSize: ThemeSizes = MEDIUM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeColors = themeColorsPalette(
        style = colorPalette,
        darkTheme = darkTheme
    )

    val themeTypography = themeTypography(fontSize = fontSize)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = themeColors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalThemeColors provides themeColors,
        LocalThemeTypography provides themeTypography,
        content = content
    )
}

@Composable
fun themeColorsPalette(
    style: ThemePaletteColors,
    darkTheme: Boolean
): ThemeColors = when {
    darkTheme -> {
        when (style) {
            GREEN -> baseDarkPalette
            PURPLE -> purpleDarkPalette
            PINK -> pinkDarkPalette
        }
    }
    else -> {
        when (style) {
            GREEN -> baseLightPalette
            PURPLE -> purpleLightPalette
            PINK -> pinkLightPalette
        }
    }
}

@Composable
fun themeTypography(fontSize: ThemeSizes): ThemeTypography = ThemeTypography(
    screenHeading = TextStyle(
        fontSize = when (fontSize) {
            SMALL -> 24.sp
            MEDIUM -> 26.sp
            BIG -> 28.sp
        },
        fontWeight = FontWeight.Bold
    ),
    cardTitle = TextStyle(
        fontSize = when (fontSize) {
            SMALL -> 16.sp
            MEDIUM -> 18.sp
            BIG -> 20.sp
        },
        fontWeight = FontWeight.Bold
    ),
    cardSubtitle = TextStyle(
        fontSize = when (fontSize) {
            SMALL -> 14.sp
            MEDIUM -> 16.sp
            BIG -> 18.sp
        }
    ),
    title = TextStyle(
        fontSize = when (fontSize) {
            SMALL -> 18.sp
            MEDIUM -> 20.sp
            BIG -> 22.sp
        },
        fontWeight = FontWeight.Bold
    ),
    subtitle = TextStyle(
        fontSize = when (fontSize) {
            SMALL -> 16.sp
            MEDIUM -> 18.sp
            BIG -> 20.sp
        }, fontWeight = FontWeight.Bold
    ),
    buttonText = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    bottomNavigationText = TextStyle(
        fontSize = when (fontSize) {
            SMALL -> 10.sp
            MEDIUM -> 12.sp
            BIG -> 14.sp
        },
        fontWeight = FontWeight.Medium
    )
)
