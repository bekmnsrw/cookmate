package com.example.cookmate.presentation.recipes.dishes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.cookmate.R
import com.example.cookmate.Screen
import com.example.cookmate.domain.dtos.MealDto

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

    CircularProgressBar(
        screenState = screenState
    )
}

@Composable
fun DishesList(
    screenState: DishesScreenState,
    navController: NavController
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(id = R.string.dishes_title),
                modifier = Modifier.padding(16.dp)
            )
        }
        items(
            screenState.dishes,
            key = { it.id }
        ) {
            ListItem(mealDto = it) { mealId ->
                navController.navigate(Screen.Recipe.createRoute(mealId))
            }
        }
    }
}

@Composable
fun ListItem(
    mealDto: MealDto,
    onClick: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .clickable {
                onClick.invoke(mealDto.id)
            }
    ) {

        Text(text = mealDto.name)
    }
}

@Composable
private fun CircularProgressBar(screenState: DishesScreenState) {
    if (screenState.isLoading) {
        CircularProgressIndicator()
    }
}
