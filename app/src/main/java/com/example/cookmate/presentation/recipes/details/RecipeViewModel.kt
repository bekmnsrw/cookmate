package com.example.cookmate.presentation.recipes.details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookmate.domain.dtos.MealDetailsDto
import com.example.cookmate.domain.usecases.GetMealDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getMealDetailsUseCase: GetMealDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeScreenState())
    val state: StateFlow<RecipeScreenState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<RecipeScreenAction?>()
    val action: SharedFlow<RecipeScreenAction?> = _action.asSharedFlow()

    fun eventHandler(event: RecipeScreenEvent) {
        when (event) {
            is RecipeScreenEvent.LoadingRecipe -> loadRecipe(event.dishId)
        }
    }

    private fun loadRecipe(dishId: String) = viewModelScope.launch {
        getMealDetailsUseCase(dishId).flowOn(Dispatchers.IO)
            .onStart { _state.emit(_state.value.copy(isLoading = true)) }
            .onCompletion { _state.emit(_state.value.copy(isLoading = false)) }
            .catch {
                _action.emit(RecipeScreenAction.ShowError)
                _state.emit(_state.value.copy(error = it))
            }
            .collect {
                _state.emit(_state.value.copy(recipe = it))
                _state.emit(_state.value.copy(isLoaded = true))
            }
    }
}

@Immutable
data class RecipeScreenState(
    val isLoading: Boolean = false,
    val recipe: MealDetailsDto? = null,
    val error: Throwable? = null,
    val isLoaded: Boolean = false
)

@Immutable
sealed interface RecipeScreenEvent {
    data class LoadingRecipe(val dishId: String) : RecipeScreenEvent
}

@Immutable
sealed interface RecipeScreenAction {
    object ShowError : RecipeScreenAction
}
