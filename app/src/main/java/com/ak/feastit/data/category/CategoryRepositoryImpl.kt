package com.ak.feastit.data.category

import com.ak.feastit.domain.category.CategoryRepository
import com.ak.feastit.domain.category.RecipeCategory
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl : CategoryRepository {

    override fun getFoodCategories(): Flow<RecipeResult<List<RecipeCategory>>> = flow {
        emit(RecipeResult.success(getStaticMealCategories()))
    }

    private fun getStaticMealCategories(): List<RecipeCategory> {
        val categoryList = mutableListOf<RecipeCategory>()
        categoryList.add(RecipeCategory(1, "Main course"))
        categoryList.add(RecipeCategory(2, "Side dish"))
        categoryList.add(RecipeCategory(3, "Bread"))
        categoryList.add(RecipeCategory(4, "Marinade"))
        categoryList.add(RecipeCategory(5, "Breakfast"))
        categoryList.add(RecipeCategory(6, "Fingerfood"))
        categoryList.add(RecipeCategory(7, "Dessert"))
        categoryList.add(RecipeCategory(8, "Soup"))
        categoryList.add(RecipeCategory(9, "Snack"))
        categoryList.add(RecipeCategory(10, "Appetizer"))
        categoryList.add(RecipeCategory(11, "Beverage"))
        categoryList.add(RecipeCategory(12, "Drink"))
        categoryList.add(RecipeCategory(13, "Salad"))
        categoryList.add(RecipeCategory(14, "Sauce"))
        return categoryList
    }
}