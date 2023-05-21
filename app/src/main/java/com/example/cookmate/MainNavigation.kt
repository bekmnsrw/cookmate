package com.example.cookmate

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cookmate.presentation.favorites.FavoritesScreen
import com.example.cookmate.presentation.recipes.categories.CategoriesScreen
import com.example.cookmate.presentation.recipes.details.RecipeScreen
import com.example.cookmate.presentation.recipes.dishes.DishesScreen
import com.example.cookmate.presentation.settings.SettingsScreen

sealed class BottomNavigationItem(
    val route: String,
    @StringRes val name: Int,
    val icon: ImageVector
) {

    object Recipes : BottomNavigationItem(
        route = "recipes",
        name = R.string.screen_recipes,
        icon = Icons.Rounded.MenuBook
    )

    object Favorites : BottomNavigationItem(
        route = "favorites",
        name = R.string.screen_favorites,
        icon = Icons.Rounded.Favorite
    )

    object Settings : BottomNavigationItem(
        route = "settings",
        name = R.string.screen_settings,
        icon = Icons.Rounded.Settings
    )
}

sealed class Screen(val route: String) {

    object Dishes : Screen(route = "{categoryName}/dishes") {
        fun createRoute(categoryName: String) = "$categoryName/dishes"
    }

    object Recipe : Screen(route = "{dishId}/recipe") {
        fun createRoute(dishId: String) = "$dishId/recipe"
    }
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    startDestination: BottomNavigationItem = BottomNavigationItem.Recipes
) {
    val items = listOf(
        BottomNavigationItem.Recipes,
        BottomNavigationItem.Favorites,
        BottomNavigationItem.Settings
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(

            ) {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(id = screen.name)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navHostController.navigate(screen.route) {
                                popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationItem.Recipes.route) { CategoriesScreen(navHostController) }
            composable(BottomNavigationItem.Favorites.route) { FavoritesScreen(navHostController) }
            composable(BottomNavigationItem.Settings.route) { SettingsScreen(navHostController) }

            composable(Screen.Dishes.route) {
                DishesScreen(
                    navHostController,
                    it.arguments?.getString("categoryName").toString()
                )
            }
            composable(Screen.Recipe.route) {
                RecipeScreen(
                    navHostController,
                    it.arguments?.getString("dishId").toString()
                )
            }
        }
    }
}
