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
    style: ThemePaletteColors = GREEN,
    textSize: ThemeSizes = MEDIUM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeColors = when {
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

    val themeTypography = ThemeTypography(
        screenHeading = TextStyle(
            fontSize = when (textSize) {
                SMALL -> 24.sp
                MEDIUM -> 28.sp
                BIG -> 32.sp
            },
            fontWeight = FontWeight.Bold
        ),
        cardTitle = TextStyle(
            fontSize = when (textSize) {
                SMALL -> 16.sp
                MEDIUM -> 18.sp
                BIG -> 20.sp
            },
            fontWeight = FontWeight.Bold
        ),
        cardSubtitle = TextStyle(
            fontSize = when (textSize) {
                SMALL -> 14.sp
                MEDIUM -> 16.sp
                BIG -> 18.sp
            }
        ),
        title = TextStyle(
            fontSize = when (textSize) {
                SMALL -> 18.sp
                MEDIUM -> 20.sp
                BIG -> 22.sp
            },
            fontWeight = FontWeight.Bold
        ),
        subtitle = TextStyle(
            fontSize = when (textSize) {
                SMALL -> 16.sp
                MEDIUM -> 18.sp
                BIG -> 20.sp
            },
            fontWeight = FontWeight.Bold
        ),
        buttonText = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        ),
        bottomNavigationText = TextStyle(
            fontSize = when (textSize) {
                SMALL -> 10.sp
                MEDIUM -> 12.sp
                BIG -> 14.sp
            },
            fontWeight = FontWeight.Medium
        )
    )

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
