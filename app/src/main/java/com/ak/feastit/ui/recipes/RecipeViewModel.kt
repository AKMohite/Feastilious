package com.ak.feastit.ui.recipes

import androidx.lifecycle.*
import com.ak.feastit.data.utils.FeastPrefManager
import com.ak.feastit.domain.category.GetCategoriesUseCase
import com.ak.feastit.domain.category.RecipeCategory
import com.ak.feastit.domain.recipelist.RandomRecipeUseCase
import com.ak.feastit.domain.recipelist.Recipe
import com.ak.feastit.domain.recipelist.SearchRecipeUseCase
import com.ak.feastit.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val searchRecipeUseCase: SearchRecipeUseCase,
        val prefManager: FeastPrefManager,
        val savedStateHandle: SavedStateHandle
): ViewModel() {

    val allCategories = MutableStateFlow<List<RecipeCategory>>(emptyList())
    val dashboardRecipes = MutableStateFlow<List<Recipe>>(emptyList())

    init {
        getCategoriesUseCase.execute(Unit).onEach { state ->
            state.data?.let { categories ->
                allCategories.value = categories
            }
        }.launchIn(viewModelScope)

        searchRecipeUseCase.execute(applyQueries()).onEach { state ->
            state.data?.let { recipes ->
                dashboardRecipes.value = recipes
            }
        }.launchIn(viewModelScope)
    }

//    ?offset=0&number=6&type=&diet=&addRecipeInformation=true&fillIngredients=true
    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
//
//        viewModelScope.launch {
//            readMealAndDietType.collect { value ->
//                mealType = value.selectedMealType
//                dietType = value.selectedDietType
//            }
//        }

        queries[QUERY_NUMBER] = DEFAULT_PAGE_SIZE
        queries[QUERY_OFFSET] = "0" // TODO pagination
        queries[QUERY_TYPE] = "" // TODO implement meal type
        queries[QUERY_DIET] = "" // TODO implement diet type
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_OFFSET] = "0" // TODO pagination
        queries[QUERY_NUMBER] = DEFAULT_PAGE_SIZE
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}