package com.example.cookmate.presentation.recipes.dishes

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookmate.domain.dtos.MealDto
import com.example.cookmate.domain.usecases.GetMealsByCategoryUseCase
import com.example.cookmate.utils.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException
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
            is DishesScreenEvent.LoadDishes -> loadDishes(event.categoryName)
        }
    }

    private fun loadDishes(categoryName: String) = viewModelScope.launch {
        getMealsByCategoryUseCase(categoryName).flowOn(Dispatchers.IO)
            .onStart { _state.emit(_state.value.copy(isLoading = true)) }
            .onCompletion { _state.emit(_state.value.copy(isLoading = false)) }
            .catch {
                _state.emit(_state.value.copy(error = it))
                when (it) {
                    is UnknownHostException -> {
                        _action.emit(DishesScreenAction.ShowError(ErrorType.NO_INTERNET_CONNECTION))
                    }
                    else -> {
                        _action.emit(DishesScreenAction.ShowError(ErrorType.OTHER))
                    }
                }
            }
            .collect { _state.emit(_state.value.copy(dishes = it.toPersistentList())) }
    }
}

@Immutable
data class DishesScreenState(
    val isLoading: Boolean = false,
    val dishes: PersistentList<MealDto> = persistentListOf(),
    val error: Throwable? = null
)

@Immutable
sealed interface DishesScreenEvent {
    data class LoadDishes(val categoryName: String) : DishesScreenEvent
}

@Immutable
sealed interface DishesScreenAction {
    data class ShowError(val errorType: ErrorType) : DishesScreenAction
}
