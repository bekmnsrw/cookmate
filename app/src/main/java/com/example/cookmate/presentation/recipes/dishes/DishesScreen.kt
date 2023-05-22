package com.example.cookmate.presentation.recipes.dishes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cookmate.R
import com.example.cookmate.Screen
import com.example.cookmate.domain.dtos.MealDto
import com.example.cookmate.ui.custom.CustomTheme

@Composable
fun DishesScreen(
    navController: NavController,
    categoryName: String,
    viewModel: DishesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    DishesContent(
        screenState = state.value,
        navController = navController,
        eventHandler = viewModel::eventHandler,
        categoryName = categoryName
    )
}

@Composable
fun DishesContent(
    screenState: DishesScreenState,
    navController: NavController,
    eventHandler: (DishesScreenEvent) -> Unit,
    categoryName: String
) {
    if (!screenState.isLoaded) {
        LaunchedEffect(Unit) {
            eventHandler.invoke(DishesScreenEvent.LoadingDishes(categoryName))
        }
    }

    DishesList(
        screenState = screenState,
        navController = navController
    )
}

@Composable
fun DishesList(
    screenState: DishesScreenState,
    navController: NavController
) {
    val dishes = screenState.dishes

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.themeColors.background)
    ) {
        items(screenState.dishes.size) {
            ListItem(mealDto = dishes[it]) { mealId ->
                navController.navigate(Screen.Recipe.createRoute(mealId))
            }
        }
    }
    CircularProgressBar(
        screenState = screenState
    )
}

@Composable
fun ListItem(
    mealDto: MealDto,
    onClick: (String) -> Unit
) {
    Card(
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = CustomTheme.themeColors.surface
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mealDto.photoUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = mealDto.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = CustomTheme.themeColors.onSurface,
                    style = CustomTheme.themeTypography.cardTitle
                )
                Button(
                    onClick = { onClick.invoke(mealDto.id) },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = CustomTheme.themeColors.primary)
                ) {
                    Text(
                        text = stringResource(id = R.string.recipe_title),
                        color = CustomTheme.themeColors.onPrimary,
                        style = CustomTheme.themeTypography.buttonText
                    )
                }
            }
        }
    }
}

@Composable
private fun CircularProgressBar(screenState: DishesScreenState) {
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
