package com.example.cookmate.presentation.recipes.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cookmate.R

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
        navController = navController,
        eventHandler = viewModel::eventHandler,
        dishId = dishId
    )
}

@Composable
fun RecipeContent(
    screenState: RecipeScreenState,
    navController: NavController,
    eventHandler: (RecipeScreenEvent) -> Unit,
    dishId: String
) {

    if (!screenState.isLoaded) {
        LaunchedEffect(Unit) {
            eventHandler.invoke(RecipeScreenEvent.LoadingRecipe(dishId))
        }
    }

    RecipeInfo(
        screenState = screenState
    )

    CircularProgressBar(
        screenState = screenState
    )
}

@Composable
fun RecipeInfo(
    screenState: RecipeScreenState
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = stringResource(id = R.string.recipe_title),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            if (screenState.recipe != null) {
                AsyncImage(
                    model = screenState.recipe.photoUrl,
                    contentDescription = null
                )

                Text(text = screenState.recipe.name)
                Text(text = screenState.recipe.category)
                Text(text = screenState.recipe.country)
                Text(text = screenState.recipe.instructions)
            }
        }
    }
}

@Composable
private fun CircularProgressBar(screenState: RecipeScreenState) {
    if (screenState.isLoading) {
        CircularProgressIndicator()
    }
}
