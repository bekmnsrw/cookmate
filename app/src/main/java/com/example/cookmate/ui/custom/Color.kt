package com.example.cookmate.ui.custom

import androidx.compose.ui.graphics.Color

internal val baseLightPalette = Colors(
    background = Color(0xfffdfdf6),
    onBackground = Color(0xff1a1c18),
    primary = Color(0xff1e6d0b),
    onPrimary = Color(0xffffffff),
    surface = Color(0xfffdfdf6),
    onSurface = Color(0xff1a1c18),
    outline = Color(0xff73796e)
)

internal val baseDarkPalette = Colors(
    background = Color(0xff1a1c18),
    onBackground = Color(0xffe2e3dc),
    primary = Color(0xff88da6e),
    onPrimary = Color(0xff073900),
    surface = Color(0xff3d4b36),
    onSurface = Color(0xffd7e7cc),
    outline = Color(0xff8d9387)
)

val purpleLightPalette = baseLightPalette.copy(
    background = Color(0xfffffbff),
    onBackground = Color(0xff1c1b1e),
    primary = Color(0xff684fa3),
    onPrimary = Color(0xffffffff),
    surface = Color(0xfffffbff),
    onSurface = Color(0xff1c1b1e),
    outline = Color(0xff7a757f)
)

val purpleDarkPalette = baseDarkPalette.copy(
    background = Color(0xff1c1b1e),
    onBackground = Color(0xffe6e1e6),
    primary = Color(0xffd1bcff),
    onPrimary = Color(0xff391e72),
    surface = Color(0xff4c4357),
    onSurface = Color(0xffebddf7),
    outline = Color(0xff948f99)
)

val pinkLightPalette = baseLightPalette.copy(
    background = Color(0xfffffbff),
    onBackground = Color(0xff201a1c),
    primary = Color(0xff8d4380),
    onPrimary = Color(0xffffffff),
    surface = Color(0xfffffbff),
    onSurface = Color(0xff201a1c),
    outline = Color(0xff80747b)
)

val pinkDarkPalette = baseDarkPalette.copy(
    background = Color(0xff201a1c),
    onBackground = Color(0xffebe0e1),
    primary = Color(0xffffb0cb),
    onPrimary = Color(0xff5f0e36),
    surface = Color(0xff56404f),
    onSurface = Color(0xfff9daed),
    outline = Color(0xff9b8d95)
)
