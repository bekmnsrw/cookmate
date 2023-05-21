package com.example.cookmate.presentation.settings

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import com.example.cookmate.ui.custom.ThemePaletteColors
import com.example.cookmate.ui.custom.ThemeSizes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsEventBus : ViewModel() {

    private val _currentSettings = MutableStateFlow(CurrentSettings())
    val currentSettings: StateFlow<CurrentSettings> = _currentSettings.asStateFlow()

    fun updateDarkMode(isDarkMode: Boolean) {
        _currentSettings.value = _currentSettings.value.copy(isDarkMode = isDarkMode)
    }

    fun updateStyle(style: ThemePaletteColors) {
        _currentSettings.value = _currentSettings.value.copy(paletteColor = style)
    }

    fun updateFontSize(textSize: ThemeSizes) {
        _currentSettings.value = _currentSettings.value.copy(textSize = textSize)
    }
}

val LocalSettingsEventBus = staticCompositionLocalOf { SettingsEventBus() }

@Immutable
data class CurrentSettings(
    val isDarkMode: Boolean = true,
    val textSize: ThemeSizes = ThemeSizes.MEDIUM,
    val paletteColor: ThemePaletteColors = ThemePaletteColors.GREEN,
)
