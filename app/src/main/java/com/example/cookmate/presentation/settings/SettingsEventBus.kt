package com.example.cookmate.presentation.settings

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookmate.ui.custom.ThemePaletteColors
import com.example.cookmate.ui.custom.ThemeSizes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsEventBus @Inject constructor() : ViewModel() {

    private val _currentSettings = MutableStateFlow(CurrentSettings())
    val currentSettings: StateFlow<CurrentSettings> = _currentSettings.asStateFlow()

    fun eventHandler(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.UpdateColorPalette -> {
                updateColorPalette(event.colorPalette)
            }
            is SettingsScreenEvent.UpdateDarkMode -> {
                updateDarkMode(event.isDarkMode)
            }
            is SettingsScreenEvent.UpdateFontSize -> {
                updateFontSize(event.fontSize)
            }
        }
    }

    private fun updateColorPalette(colorPalette: ThemePaletteColors) = viewModelScope.launch {
        _currentSettings.emit(_currentSettings.value.copy(colorPalette = colorPalette))
    }

    private fun updateDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        _currentSettings.emit(_currentSettings.value.copy(isDarkMode = isDarkMode))
    }

    private fun updateFontSize(fontSize: ThemeSizes) = viewModelScope.launch {
        _currentSettings.emit(_currentSettings.value.copy(fontSize = fontSize))
    }
}


@Immutable
data class CurrentSettings(
    val isDarkMode: Boolean = true,
    val fontSize: ThemeSizes = ThemeSizes.MEDIUM,
    val colorPalette: ThemePaletteColors = ThemePaletteColors.GREEN,
)

@Immutable
sealed interface SettingsScreenEvent {
    data class UpdateDarkMode(val isDarkMode: Boolean) : SettingsScreenEvent
    data class UpdateFontSize(val fontSize: ThemeSizes) : SettingsScreenEvent
    data class UpdateColorPalette(val colorPalette: ThemePaletteColors) : SettingsScreenEvent
}
