package com.example.cookmate.presentation.recipes.categories

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookmate.domain.dtos.CategoryDto
import com.example.cookmate.domain.usecases.GetCategoriesUseCase
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
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesScreenState())
    val state: StateFlow<CategoriesScreenState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<CategoriesScreenAction?>()
    val action: SharedFlow<CategoriesScreenAction?> = _action.asSharedFlow()

    fun eventHandler(event: CategoriesScreenEvent) {
        when (event) {
            is CategoriesScreenEvent.LoadCategories -> loadCategories()
        }
    }

    private fun loadCategories() = viewModelScope.launch {
        getCategoriesUseCase().flowOn(Dispatchers.IO)
            .onStart { _state.emit(_state.value.copy(isLoading = true)) }
            .onCompletion { _state.emit(_state.value.copy(isLoading = false)) }
            .catch {
                _state.emit(_state.value.copy(error = it))
                when (it) {
                    is UnknownHostException -> {
                        _action.emit(CategoriesScreenAction.ShowError(ErrorType.NO_INTERNET_CONNECTION))
                    }
                    else -> {
                        _action.emit(CategoriesScreenAction.ShowError(ErrorType.OTHER))
                    }
                }
            }
            .collect { _state.emit(_state.value.copy(categories = it.toPersistentList())) }
    }
}

@Immutable
data class CategoriesScreenState(
    val isLoading: Boolean = false,
    val categories: PersistentList<CategoryDto> = persistentListOf(),
    val error: Throwable? = null
)

@Immutable
sealed interface CategoriesScreenEvent {
    object LoadCategories : CategoriesScreenEvent
}

@Immutable
sealed interface CategoriesScreenAction {
    data class ShowError(val errorType: ErrorType) : CategoriesScreenAction
}
