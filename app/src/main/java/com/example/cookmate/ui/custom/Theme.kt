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
import com.example.cookmate.ui.custom.PaletteColors.*
import com.example.cookmate.ui.custom.Sizes.*

@Composable
fun Theme(
    colorPalette: PaletteColors = GREEN,
    fontSize: Sizes = MEDIUM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = colorsPalette(
        style = colorPalette,
        darkTheme = darkTheme
    )

    val typography = typography(fontSize = fontSize)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content
    )
}

@Composable
fun colorsPalette(
    style: PaletteColors,
    darkTheme: Boolean
): Colors = when {
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
fun typography(fontSize: Sizes): Typography =
    Typography(
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
