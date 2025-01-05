package com.ak.feastit.ui.favorites

import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class FavoriteViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val repository: RecipesRepository
): BaseViewModel(dispatcher) {

    private val _state = MutableStateFlow(FavoriteState())
    val state = _state.asStateFlow()

    init {
        observeFavoriteRecipes()
    }

    private fun observeFavoriteRecipes() {
        repository.observeFavoriteRecipes()
            .onEach { recipes ->
                _state.update { currentState -> currentState.copy(recipes = recipes) }
            }
            .launchIn(uiScope)
    }

}
