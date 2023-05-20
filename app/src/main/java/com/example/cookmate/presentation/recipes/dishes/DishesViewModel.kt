package com.example.cookmate.presentation.recipes.dishes

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookmate.domain.dtos.MealDto
import com.example.cookmate.domain.usecases.GetMealsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishesViewModel @Inject constructor(
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DishesScreenState())
    val state: StateFlow<DishesScreenState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<DishesScreenAction?>()
    val action: SharedFlow<DishesScreenAction?> = _action.asSharedFlow()

    fun eventHandler(event: DishesScreenEvent) {
        when (event) {
            is DishesScreenEvent.LoadingDishes -> loadDishes(event.categoryName)
        }
    }

    private fun loadDishes(categoryName: String) = viewModelScope.launch {
        getMealsByCategoryUseCase(categoryName).flowOn(Dispatchers.IO)
            .onStart { _state.emit(_state.value.copy(isLoading = true)) }
            .onCompletion { _state.emit(_state.value.copy(isLoading = false)) }
            .catch {
                _action.emit(DishesScreenAction.ShowError)
                _state.emit(_state.value.copy(error = it))
            }
            .collect {
                _state.emit(_state.value.copy(dishes = it.toPersistentList()))
                _state.emit(_state.value.copy(isLoaded = true))
            }
    }
}

@Immutable
data class DishesScreenState(
    val isLoading: Boolean = false,
    val dishes: PersistentList<MealDto> = persistentListOf(),
    val error: Throwable? = null,
    val isLoaded: Boolean = false
)

@Immutable
sealed interface DishesScreenEvent {
    data class LoadingDishes(val categoryName: String) : DishesScreenEvent
}

@Immutable
sealed interface DishesScreenAction {
    object ShowError : DishesScreenAction
}
