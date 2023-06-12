package com.example.cookmate.presentation.recipes.dishes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cookmate.R
import com.example.cookmate.Screen
import com.example.cookmate.domain.dtos.MealDto
import com.example.cookmate.presentation.recipes.categories.rememberLifecycleEvent
import com.example.cookmate.ui.custom.CustomTheme
import com.example.cookmate.utils.ErrorType
import com.example.cookmate.utils.handleError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DishesScreen(
    navController: NavController,
    categoryName: String,
    viewModel: DishesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    DishesContent(
        screenState = state.value,
        navController = navController,
        eventHandler = viewModel::eventHandler,
        categoryName = categoryName,
        scaffoldState = scaffoldState
    )

    CategoriesScreenEvent(
        screenAction = action,
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        context = LocalContext.current
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DishesContent(
    screenState: DishesScreenState,
    navController: NavController,
    eventHandler: (DishesScreenEvent) -> Unit,
    categoryName: String,
    scaffoldState: ScaffoldState
) {
    val lifecycleOwner = rememberLifecycleEvent()

    LaunchedEffect(lifecycleOwner) {
        if (lifecycleOwner == Lifecycle.Event.ON_RESUME && screenState.dishes.isEmpty()) {
            eventHandler.invoke(DishesScreenEvent.LoadDishes(categoryName))
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    actionColor = CustomTheme.colors.primary,
                    snackbarData = data
                )
            }
        }
    ) {
        DishesList(
            screenState = screenState
        ) {
            navController.navigate(it)
        }
    }
}

@Composable
private fun CategoriesScreenEvent(
    screenAction: DishesScreenAction?,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    context: Context
) {
    when (screenAction) {
        null -> Unit
        is DishesScreenAction.ShowError -> {
            val errorType: ErrorType = screenAction.errorType
            val handledError: Pair<String, String> = handleError(errorType)

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = handledError.first,
                        actionLabel = handledError.second,
                        duration = SnackbarDuration.Long
                    )
                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            when (errorType) {
                                ErrorType.NO_INTERNET_CONNECTION -> {
                                    context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                                }
                                ErrorType.OTHER -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DishesList(
    screenState: DishesScreenState,
    onItemClick: (String) -> Unit
) {
    val dishes = screenState.dishes

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.background)
    ) {
        items(screenState.dishes.size) {
            ListItem(mealDto = dishes[it]) { mealId ->
                onItemClick.invoke(Screen.Recipe.createRoute(mealId))
            }
        }
    }
    CircularProgressBar(
        isLoading = screenState.isLoading
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
        backgroundColor = CustomTheme.colors.surface
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
                    color = CustomTheme.colors.onSurface,
                    style = CustomTheme.typography.cardTitle
                )
                Button(
                    onClick = { onClick.invoke(mealDto.id) },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = CustomTheme.colors.primary)
                ) {
                    Text(
                        text = stringResource(id = R.string.recipe_title),
                        color = CustomTheme.colors.onPrimary,
                        style = CustomTheme.typography.buttonText
                    )
                }
            }
        }
    }
}

@Composable
private fun CircularProgressBar(isLoading: Boolean) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = CustomTheme.colors.primary
            )
        }
    }
}
