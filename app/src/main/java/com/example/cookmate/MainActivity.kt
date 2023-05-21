package com.example.cookmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.cookmate.presentation.settings.LocalSettingsEventBus
import com.example.cookmate.presentation.settings.SettingsEventBus
import com.example.cookmate.ui.custom.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val settingsEventBus = remember { SettingsEventBus() }
            val currentSettings = settingsEventBus.currentSettings.collectAsState().value

            Theme(
                style = currentSettings.paletteColor,
                textSize = currentSettings.textSize,
                darkTheme = currentSettings.isDarkMode,
            ) {
                CompositionLocalProvider(
                    LocalSettingsEventBus provides settingsEventBus
                ) {
                    NavigationHost()
                }
            }
        }
    }
}
