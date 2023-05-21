package com.example.cookmate.presentation.recipes.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.example.cookmate.R
import com.example.cookmate.ui.custom.CustomTheme

@Composable
fun RecipeScreen(
    navController: NavController,
    dishId: String,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    RecipeContent(
        screenState = state.value,
        eventHandler = viewModel::eventHandler,
        dishId = dishId,
        navController = navController
    )
}

@Composable
fun RecipeContent(
    screenState: RecipeScreenState,
    eventHandler: (RecipeScreenEvent) -> Unit,
    dishId: String,
    navController: NavController
) {
    if (!screenState.isLoaded) {
        LaunchedEffect(Unit) {
            eventHandler.invoke(RecipeScreenEvent.LoadingRecipe(dishId))
        }
    }

    RecipeInfo(
        screenState = screenState,
        navController = navController
    )
}

@Composable
fun RecipeInfo(
    screenState: RecipeScreenState,
    navController: NavController
) {
    val recipe = screenState.recipe

    Column(
        modifier = Modifier.background(CustomTheme.themeColors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.themeColors.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = CustomTheme.themeColors.onPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                if (recipe != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(recipe.photoUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .scale(Scale.FILL)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = recipe.name,
                            modifier = Modifier.padding(bottom = 16.dp),
                            color = CustomTheme.themeColors.onBackground,
                            style = CustomTheme.themeTypography.screenHeading
                        )
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.RestaurantMenu,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp),
                                tint = CustomTheme.themeColors.primary
                            )
                            Text(
                                text = recipe.category,
                                color = CustomTheme.themeColors.onBackground,
                                style = CustomTheme.themeTypography.title
                            )
                        }
                        Row(
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Public,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp),
                                tint = CustomTheme.themeColors.primary
                            )
                            Text(
                                text = recipe.country,
                                color = CustomTheme.themeColors.onBackground,
                                style = CustomTheme.themeTypography.title,
                            )
                        }
                        Divider(
                            color = CustomTheme.themeColors.primary
                        )
                        Text(
                            text = recipe.instructions,
                            modifier = Modifier.padding(top = 16.dp),
                            color = CustomTheme.themeColors.onBackground,
                            style = CustomTheme.themeTypography.subtitle
                        )
                    }
                }
            }
        }
        CircularProgressBar(
            screenState = screenState
        )
    }
}

@Composable
private fun CircularProgressBar(screenState: RecipeScreenState) {
    if (screenState.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = CustomTheme.themeColors.primary
            )
        }
    }
}
