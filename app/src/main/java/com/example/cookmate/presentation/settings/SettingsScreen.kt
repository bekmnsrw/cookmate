package com.example.cookmate.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cookmate.R
import com.example.cookmate.ui.custom.*
import com.example.cookmate.ui.custom.baseDarkPalette
import com.example.cookmate.ui.custom.baseLightPalette

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsEventBus = hiltViewModel()
) {
    val settingsEventBus = LocalSettingsEventBus.current
    val currentSettings = settingsEventBus.currentSettings.collectAsState().value

    SettingsContent(currentSettings, settingsEventBus)
}

@Composable
fun SettingsContent(
    currentSettings: CurrentSettings,
    settingsEventBus: SettingsEventBus
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.themeColors.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {
            Text(
                text = stringResource(id = R.string.settings_title),
                color = CustomTheme.themeColors.onBackground,
                style = CustomTheme.themeTypography.screenHeading,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        item {
            DarkModeCard(
                currentSettings = currentSettings,
                settingsEventBus = settingsEventBus
            )
        }
        item {
            ColorPaletteCard(
                currentSettings = currentSettings,
                settingsEventBus = settingsEventBus
            )
        }
        item {
            FontSizeCard(
                currentSettings = currentSettings,
                settingsEventBus = settingsEventBus
            )
        }
    }
}

@Composable
fun DarkModeCard(
    currentSettings: CurrentSettings,
    settingsEventBus: SettingsEventBus
) {
    Card(
        backgroundColor = CustomTheme.themeColors.surface,
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.dark_mode),
                color = CustomTheme.themeColors.onSurface,
                style = CustomTheme.themeTypography.cardTitle
            )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = currentSettings.isDarkMode,
                    onCheckedChange = {
                        settingsEventBus.updateDarkMode(!currentSettings.isDarkMode)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CustomTheme.themeColors.onPrimary,
                        checkedTrackColor = CustomTheme.themeColors.primary,
                        uncheckedThumbColor = CustomTheme.themeColors.outline,
                        uncheckedTrackColor = CustomTheme.themeColors.primary,
                    )
                )
            }
        }
    }
}

@Composable
fun ColorPaletteCard(
    currentSettings: CurrentSettings,
    settingsEventBus: SettingsEventBus
) {
    Card(
        backgroundColor = CustomTheme.themeColors.surface,
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.color_palette),
                color = CustomTheme.themeColors.onSurface,
                style = CustomTheme.themeTypography.cardTitle,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorCard(
                    color = if (currentSettings.isDarkMode) baseDarkPalette.primary else baseLightPalette.primary,
                    onClick = { settingsEventBus.updateStyle(ThemePaletteColors.GREEN) }
                )
                ColorCard(
                    color = if (currentSettings.isDarkMode) purpleDarkPalette.primary else purpleLightPalette.primary,
                    onClick = { settingsEventBus.updateStyle(ThemePaletteColors.PURPLE) }
                )
                ColorCard(
                    color = if (currentSettings.isDarkMode) pinkDarkPalette.primary else pinkLightPalette.primary,
                    onClick = { settingsEventBus.updateStyle(ThemePaletteColors.PINK) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColorCard(
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier.size(56.dp, 56.dp),
        backgroundColor = color,
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp)
    ) {}
}

@Composable
fun FontSizeCard(
    currentSettings: CurrentSettings,
    settingsEventBus: SettingsEventBus
) {
    Card(
        backgroundColor = CustomTheme.themeColors.surface,
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.font_size),
                color = CustomTheme.themeColors.onSurface,
                style = CustomTheme.themeTypography.cardTitle,
                modifier = Modifier.padding(
                    bottom = 16.dp,
                    start = 16.dp
                )
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                CustomRadioButton(
                    isSelected = currentSettings.textSize == ThemeSizes.BIG,
                    onClick = { settingsEventBus.updateFontSize(ThemeSizes.BIG) },
                    label = stringResource(id = R.string.font_big)
                )
                CustomRadioButton(
                    isSelected = currentSettings.textSize == ThemeSizes.MEDIUM,
                    onClick = { settingsEventBus.updateFontSize(ThemeSizes.MEDIUM) },
                    label = stringResource(id = R.string.font_medium)
                )
                CustomRadioButton(
                    isSelected = currentSettings.textSize == ThemeSizes.SMALL,
                    onClick = { settingsEventBus.updateFontSize(ThemeSizes.SMALL) },
                    label = stringResource(id = R.string.font_small)
                )
            }
        }
    }
}

@Composable
fun CustomRadioButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onClick() },
            colors = RadioButtonDefaults.colors(
                selectedColor = CustomTheme.themeColors.primary,
                unselectedColor = CustomTheme.themeColors.outline
            )
        )
        Text(
            text = label,
            color = CustomTheme.themeColors.onSurface,
            style = CustomTheme.themeTypography.cardSubtitle,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
