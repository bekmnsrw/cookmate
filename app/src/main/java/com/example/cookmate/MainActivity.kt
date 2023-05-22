package com.example.cookmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.cookmate.presentation.settings.SettingsEventBus
import com.example.cookmate.ui.custom.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SettingsEventBus by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsEventBus = remember { viewModel }
            val currentSettings = settingsEventBus.currentSettings.collectAsState().value

            Theme(
                style = currentSettings.colorPalette,
                textSize = currentSettings.fontSize,
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

val LocalSettingsEventBus = staticCompositionLocalOf<SettingsEventBus> {
    error("No settings event bus provided")
}
