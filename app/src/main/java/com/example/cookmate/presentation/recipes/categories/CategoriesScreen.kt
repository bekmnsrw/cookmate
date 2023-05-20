package com.example.cookmate.presentation.recipes.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cookmate.R
import com.example.cookmate.Screen
import com.example.cookmate.domain.dtos.CategoryDto

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    CategoriesContent(
        screenState = state.value,
        navController = navController
    )

    CategoriesScreenActions(
        screenAction = action
    )
}

@Composable
fun CategoriesContent(
    screenState: CategoriesScreenState,
    navController: NavController
) {

    CategoriesList(screenState, navController)
    CircularProgressBar(screenState)
}

@Composable
private fun CategoriesScreenActions(
    screenAction: CategoriesScreenAction?
) {
    when (screenAction) {
        null -> Unit
        is CategoriesScreenAction.ShowError -> TODO()
    }
}

@Composable
fun CategoriesList(
    screenState: CategoriesScreenState,
    navController: NavController
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(id = R.string.categories_title),
                modifier = Modifier.padding(16.dp)
            )
        }
        items(
            screenState.categories,
            key = { it.id }
        ) {
            ListItem(categoryDto = it) { categoryName ->
                navController.navigate(Screen.Dishes.createRoute(categoryName))
            }
        }
    }
}

@Composable
fun ListItem(
    categoryDto: CategoryDto,
    onClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .clickable {
                onClick.invoke(categoryDto.name)
            }
    ) {

        Text(text = categoryDto.name)
        Text(text = categoryDto.description)
    }
}

@Composable
private fun CircularProgressBar(screenState: CategoriesScreenState) {
    if (screenState.isLoading) { 
        CircularProgressIndicator()
    }
}
