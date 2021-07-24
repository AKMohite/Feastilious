package com.ak.feastit.ui.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.data.utils.FeastPrefManager
import com.ak.feastit.domain.usecase.GetCategoriesUseCase
import com.ak.feastit.domain.model.RecipeCategory
import com.ak.feastit.domain.usecase.MealTypeRecipeUseCase
import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.usecase.SearchRecipeUseCase
import com.ak.feastit.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlin.collections.set

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val searchRecipeUseCase: SearchRecipeUseCase,
    private val mealTypeRecipeUseCase: MealTypeRecipeUseCase,
    val prefManager: FeastPrefManager,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val allCategories = MutableStateFlow<List<RecipeCategory>>(emptyList())
    val dashboardRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val selectedCategory = MutableStateFlow("")

    init {
        getCategoriesUseCase.execute(Unit).onEach { state ->
            state.data?.let { categories ->
                allCategories.value = categories
            }
        }.launchIn(viewModelScope)

        searchRecipe("")
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
        queries[QUERY_TYPE] = selectedCategory.value // TODO implement meal type
        queries[QUERY_DIET] = "" // TODO implement diet type
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    private fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_OFFSET] = "0" // TODO pagination
        queries[QUERY_NUMBER] = DEFAULT_PAGE_SIZE
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun searchRecipe(searchQuery: String) {
        selectedCategory.value = ""
        searchRecipeUseCase.execute(applySearchQuery(searchQuery = searchQuery))
                .onEach {  recipeResult ->
                    recipeResult.data?.let { recipes ->
                        dashboardRecipes.value = recipes
                    }
                }
                .launchIn(viewModelScope)
    }

    fun searchByCategory(mealCategory: String) {
        if (!mealCategory.isBlank()) {
            selectedCategory.value = mealCategory
            getRecipesByMealType()
        } else
            searchRecipe("")

    }

    private fun getRecipesByMealType() {
        mealTypeRecipeUseCase.execute(applyQueries())
                .onEach {  recipeResult ->
                    recipeResult.data?.let { recipes ->
                        dashboardRecipes.value = recipes
                    }
                }
                .launchIn(viewModelScope)
    }
}