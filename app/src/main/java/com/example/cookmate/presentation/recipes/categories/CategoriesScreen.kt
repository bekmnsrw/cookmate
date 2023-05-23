package com.example.cookmate.presentation.recipes.categories

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cookmate.R
import com.example.cookmate.Screen
import com.example.cookmate.domain.dtos.CategoryDto
import com.example.cookmate.ui.custom.CustomTheme
import com.example.cookmate.utils.ErrorType
import com.example.cookmate.utils.handleError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    CategoriesContent(
        screenState = state.value,
        navController = navController,
        scaffoldState = scaffoldState,
        eventHandler = viewModel::eventHandler
    )

    CategoriesScreenActions(
        screenAction = action,
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        context = LocalContext.current
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CategoriesContent(
    screenState: CategoriesScreenState,
    navController: NavController,
    scaffoldState: ScaffoldState,
    eventHandler: (CategoriesScreenEvent) -> Unit
) {
    val lifecycleOwner = rememberLifecycleEvent()

    LaunchedEffect(lifecycleOwner) {
        if (lifecycleOwner == Lifecycle.Event.ON_RESUME && screenState.categories.isEmpty()) {
            eventHandler.invoke(CategoriesScreenEvent.LoadCategories)
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
        CategoriesList(
            screenState = screenState,
            navController = navController
        )
    }
}

@Composable
private fun CategoriesScreenActions(
    screenAction: CategoriesScreenAction?,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    context: Context
) {
    when (screenAction) {
        null -> Unit
        is CategoriesScreenAction.ShowError -> {
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
fun CategoriesList(
    screenState: CategoriesScreenState,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.themeColors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            screenState.categories,
            key = { it.id }
        ) {
            ListItem(categoryDto = it) { categoryName ->
                navController.navigate(Screen.Dishes.createRoute(categoryName))
            }
        }
    }
    CircularProgressBar(
        screenState = screenState
    )
}

@Composable
fun ListItem(
    categoryDto: CategoryDto,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(categoryDto.name) },
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = CustomTheme.themeColors.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(categoryDto.photoUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 24.dp
                    )
            ) {
                Text(
                    text = categoryDto.name,
                    color = CustomTheme.themeColors.onSurface,
                    style = CustomTheme.themeTypography.cardTitle
                )
                Text(
                    text = categoryDto.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = CustomTheme.themeColors.onSurface,
                    style = CustomTheme.themeTypography.cardSubtitle
                )
            }
        }
    }
}

@Composable
private fun CircularProgressBar(screenState: CategoriesScreenState) {
    if (screenState.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator(
                color = CustomTheme.themeColors.primary
            )
        }
    }
}

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> state = event }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    return state
}
