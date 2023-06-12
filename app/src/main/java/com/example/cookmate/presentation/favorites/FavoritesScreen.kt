package com.example.cookmate.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cookmate.ui.custom.CustomTheme

@Composable
fun FavoritesScreen(
    navController: NavController
) {

    FavoritesContent()
}

@Composable
fun FavoritesContent() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.background)
    ) {}
}
