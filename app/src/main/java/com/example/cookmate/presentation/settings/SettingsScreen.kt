package com.example.cookmate.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cookmate.LocalSettingsEventBus
import com.example.cookmate.R
import com.example.cookmate.ui.custom.*

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsEventBus = hiltViewModel()
) {
    val currentSettingsState = viewModel.currentSettings.collectAsStateWithLifecycle()
    val settingsEventBus = LocalSettingsEventBus.current

    SettingsContent(
        settingsEventBus,
        currentSettingsState.value,
        viewModel::eventHandler
    )
}

@Composable
fun SettingsContent(
    settingsEventBus: SettingsEventBus,
    currentSettingsState: CurrentSettings,
    eventHandler: (SettingsScreenEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            DarkModeCard(
                settingsEventBus = settingsEventBus,
                currentSettingsState = currentSettingsState,
                eventHandler = eventHandler
            )
        }
        item {
            ColorPaletteCard(
                currentSettingsState = currentSettingsState,
                settingsEventBus = settingsEventBus,
                eventHandler = eventHandler
            )
        }
        item {
            FontSizeCard(
                currentSettingsState = currentSettingsState,
                settingsEventBus = settingsEventBus,
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
fun DarkModeCard(
    settingsEventBus: SettingsEventBus,
    currentSettingsState: CurrentSettings,
    eventHandler: (SettingsScreenEvent) -> Unit
) {
    Card(
        backgroundColor = CustomTheme.colors.surface,
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
                color = CustomTheme.colors.onSurface,
                style = CustomTheme.typography.cardTitle
            )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = currentSettingsState.isDarkMode,
                    onCheckedChange = {
                        settingsEventBus.eventHandler(
                            SettingsScreenEvent.UpdateDarkMode(!currentSettingsState.isDarkMode)
                        )
                        eventHandler.invoke(
                            SettingsScreenEvent.UpdateDarkMode(!currentSettingsState.isDarkMode)
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CustomTheme.colors.onPrimary,
                        checkedTrackColor = CustomTheme.colors.primary,
                        uncheckedThumbColor = CustomTheme.colors.outline,
                        uncheckedTrackColor = CustomTheme.colors.primary,
                    )
                )
            }
        }
    }
}

@Composable
fun ColorPaletteCard(
    currentSettingsState: CurrentSettings,
    settingsEventBus: SettingsEventBus,
    eventHandler: (SettingsScreenEvent) -> Unit
) {
    Card(
        backgroundColor = CustomTheme.colors.surface,
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
                color = CustomTheme.colors.onSurface,
                style = CustomTheme.typography.cardTitle,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorCard(
                    settingsEventBus = settingsEventBus,
                    colorPalette = PaletteColors.GREEN,
                    eventHandler = eventHandler,
                    currentSettingsState = currentSettingsState
                )
                ColorCard(
                    settingsEventBus = settingsEventBus,
                    colorPalette = PaletteColors.PURPLE,
                    eventHandler = eventHandler,
                    currentSettingsState = currentSettingsState
                )
                ColorCard(
                    settingsEventBus = settingsEventBus,
                    colorPalette = PaletteColors.PINK,
                    eventHandler = eventHandler,
                    currentSettingsState = currentSettingsState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColorCard(
    settingsEventBus: SettingsEventBus,
    colorPalette: PaletteColors,
    eventHandler: (SettingsScreenEvent) -> Unit,
    currentSettingsState: CurrentSettings
) {
    Card(
        modifier = Modifier.size(56.dp, 56.dp),
        backgroundColor = if (currentSettingsState.isDarkMode) {
            when (colorPalette) {
                PaletteColors.GREEN -> baseDarkPalette.primary
                PaletteColors.PURPLE -> purpleDarkPalette.primary
                PaletteColors.PINK -> pinkDarkPalette.primary
            }
        } else {
            when (colorPalette) {
                PaletteColors.GREEN -> baseLightPalette.primary
                PaletteColors.PURPLE -> purpleLightPalette.primary
                PaletteColors.PINK -> pinkLightPalette.primary
            }
        },
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        onClick = {
            settingsEventBus.eventHandler(
                SettingsScreenEvent.UpdateColorPalette(colorPalette)
            )
            eventHandler.invoke(
                SettingsScreenEvent.UpdateColorPalette(colorPalette)
            )
        }
    ) {}
}

@Composable
fun FontSizeCard(
    currentSettingsState: CurrentSettings,
    settingsEventBus: SettingsEventBus,
    eventHandler: (SettingsScreenEvent) -> Unit
) {
    Card(
        backgroundColor = CustomTheme.colors.surface,
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
                color = CustomTheme.colors.onSurface,
                style = CustomTheme.typography.cardTitle,
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
                    label = stringResource(id = R.string.font_big),
                    currentSettingsState = currentSettingsState,
                    sizes = Sizes.BIG,
                    settingsEventBus = settingsEventBus,
                    eventHandler = eventHandler
                )
                CustomRadioButton(
                    label = stringResource(id = R.string.font_medium),
                    currentSettingsState = currentSettingsState,
                    sizes = Sizes.MEDIUM,
                    settingsEventBus = settingsEventBus,
                    eventHandler = eventHandler
                )
                CustomRadioButton(
                    label = stringResource(id = R.string.font_small),
                    currentSettingsState = currentSettingsState,
                    sizes = Sizes.SMALL,
                    settingsEventBus = settingsEventBus,
                    eventHandler = eventHandler
                )
            }
        }
    }
}

@Composable
private fun CustomRadioButton(
    label: String,
    currentSettingsState: CurrentSettings,
    sizes: Sizes,
    settingsEventBus: SettingsEventBus,
    eventHandler: (SettingsScreenEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentSettingsState.fontSize == sizes,
            colors = RadioButtonDefaults.colors(
                selectedColor = CustomTheme.colors.primary,
                unselectedColor = CustomTheme.colors.outline
            ),
            onClick = {
                settingsEventBus.eventHandler(
                    SettingsScreenEvent.UpdateFontSize(sizes)
                )
                eventHandler.invoke(
                    SettingsScreenEvent.UpdateFontSize(sizes)
                )
            }
        )
        Text(
            text = label,
            color = CustomTheme.colors.onSurface,
            style = CustomTheme.typography.cardSubtitle,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
