package com.ak.feastit.ui.legacyrecipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.data.utils.FeastPrefManager
import com.ak.feastit.utils.DEFAULT_PAGE_SIZE
import com.ak.feastit.utils.QUERY_ADD_RECIPE_INFORMATION
import com.ak.feastit.utils.QUERY_DIET
import com.ak.feastit.utils.QUERY_FILL_INGREDIENTS
import com.ak.feastit.utils.QUERY_NUMBER
import com.ak.feastit.utils.QUERY_OFFSET
import com.ak.feastit.utils.QUERY_SEARCH
import com.mak.feastit.domain.model.MealType
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.legacyusecase.LegacyCategoriesUseCase
import com.mak.feastit.domain.legacyusecase.LegacyMealTypeRecipeUseCase
import com.mak.feastit.domain.legacyusecase.LegacySearchRecipeUseCase
import com.mak.feastit.domain.model.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class LegacyRecipeViewModel @Inject constructor(
    getCategoriesUseCase: LegacyCategoriesUseCase,
    private val searchRecipeUseCase: LegacySearchRecipeUseCase,
    private val mealTypeRecipeUseCase: LegacyMealTypeRecipeUseCase,
    val prefManager: FeastPrefManager,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val allCategories = MutableStateFlow<List<MealType>>(emptyList())
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
        queries[QUERY_OFFSET] = "0"
        queries[QUERY_TYPE] = selectedCategory.value
        queries[QUERY_DIET] = ""
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    private fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_OFFSET] = "0"
        queries[QUERY_NUMBER] = DEFAULT_PAGE_SIZE
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun searchRecipe(searchQuery: String) {
        selectedCategory.value = ""
        searchRecipeUseCase(applySearchQuery(searchQuery = searchQuery))
                .onEach {  recipeResult ->
                    recipeResult.data?.let { recipes ->
                        dashboardRecipes.value = recipes
                    }
                }
                .launchIn(viewModelScope)
    }

    fun searchByCategory(mealCategory: String) {
        if (mealCategory.isNotBlank()) {
            selectedCategory.value = mealCategory
            getRecipesByMealType()
        } else
            searchRecipe("")

    }

    private fun getRecipesByMealType() {
        mealTypeRecipeUseCase(applyQueries())
                .onEach {  recipeResult ->
                    recipeResult.data?.let { recipes ->
                        dashboardRecipes.value = recipes
                    }
                }
                .launchIn(viewModelScope)
    }
}