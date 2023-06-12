package com.example.cookmate.presentation.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookmate.ui.custom.PaletteColors
import com.example.cookmate.ui.custom.Sizes
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

    private fun updateColorPalette(colorPalette: PaletteColors) = viewModelScope.launch {
        _currentSettings.emit(_currentSettings.value.copy(colorPalette = colorPalette))
    }

    private fun updateDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        _currentSettings.emit(_currentSettings.value.copy(isDarkMode = isDarkMode))
    }

    private fun updateFontSize(fontSize: Sizes) = viewModelScope.launch {
        _currentSettings.emit(_currentSettings.value.copy(fontSize = fontSize))
    }
}

@Immutable
data class CurrentSettings(
    val isDarkMode: Boolean = true,
    val fontSize: Sizes = Sizes.MEDIUM,
    val colorPalette: PaletteColors = PaletteColors.GREEN,
)

@Immutable
sealed interface SettingsScreenEvent {
    data class UpdateDarkMode(val isDarkMode: Boolean) : SettingsScreenEvent
    data class UpdateFontSize(val fontSize: Sizes) : SettingsScreenEvent
    data class UpdateColorPalette(val colorPalette: PaletteColors) : SettingsScreenEvent
}
