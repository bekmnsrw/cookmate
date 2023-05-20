package com.example.cookmate.di

import com.example.cookmate.data.RecipeApi
import com.example.cookmate.data.RecipeRepositoryImpl
import com.example.cookmate.domain.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module(includes = [BindRecipeRepository::class])
@InstallIn(ActivityComponent::class)
class RecipeModule {

    @Provides
    fun provideRecipeRepository(
        recipeApi: RecipeApi
    ): RecipeRepository = RecipeRepositoryImpl(recipeApi)
}

@Module
@InstallIn(SingletonComponent::class)
interface BindRecipeRepository {

    @Binds
    fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
}
