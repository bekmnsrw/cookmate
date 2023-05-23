package com.example.cookmate.presentation.recipes.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cookmate.R
import com.example.cookmate.presentation.recipes.categories.rememberLifecycleEvent
import com.example.cookmate.ui.custom.CustomTheme
import com.example.cookmate.utils.ErrorType
import com.example.cookmate.utils.handleError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(
    navController: NavController,
    dishId: String,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    RecipeContent(
        screenState = state.value,
        eventHandler = viewModel::eventHandler,
        dishId = dishId,
        scaffoldState = scaffoldState
    )

    RecipeScreenActions(
        screenAction = action,
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        context = LocalContext.current
    )
}

@Composable
private fun RecipeScreenActions(
    screenAction: RecipeScreenAction?,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    context: Context
) {
    when (screenAction) {
        null -> Unit
        is RecipeScreenAction.ShowError -> {
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecipeContent(
    screenState: RecipeScreenState,
    eventHandler: (RecipeScreenEvent) -> Unit,
    dishId: String,
    scaffoldState: ScaffoldState
) {
    val lifecycleOwner = rememberLifecycleEvent()

    LaunchedEffect(lifecycleOwner) {
        if (lifecycleOwner == Lifecycle.Event.ON_RESUME && screenState.recipe == null) {
            eventHandler.invoke(RecipeScreenEvent.LoadingRecipe(dishId))
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    actionColor = CustomTheme.themeColors.primary,
                    snackbarData = data
                )
            }
        }
    ) {
        RecipeInfo(
            screenState = screenState
        )
    }
}

@Composable
fun RecipeInfo(screenState: RecipeScreenState) {
    val recipe = screenState.recipe
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(CustomTheme.themeColors.background)
    ) {
        item {
            if (recipe != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.photoUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = recipe.name,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = CustomTheme.themeColors.onBackground,
                        style = CustomTheme.themeTypography.screenHeading
                    )
                    TextWithIcon(
                        imageVector = Icons.Rounded.RestaurantMenu,
                        text = recipe.category
                    )
                    TextWithIcon(
                        imageVector = Icons.Rounded.Public,
                        text = recipe.country
                    )
                    Divider(
                        color = CustomTheme.themeColors.primary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Text(
                        text = recipe.instructions,
                        color = CustomTheme.themeColors.onBackground,
                        style = CustomTheme.themeTypography.subtitle
                    )
                }
            }
        }
    }
    CircularProgressBar(screenState = screenState)
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

@Composable
private fun TextWithIcon(
    imageVector: ImageVector,
    text: String
) {
    Row {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = CustomTheme.themeColors.primary
        )
        Text(
            text = text,
            color = CustomTheme.themeColors.onBackground,
            style = CustomTheme.themeTypography.title,
        )
    }
}
