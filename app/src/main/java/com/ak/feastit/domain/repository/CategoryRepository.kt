package com.ak.feastit.domain.repository

import com.ak.feastit.domain.model.MealType
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepository @Inject constructor() : ICategoryRepository {

    override fun getFoodCategories(): Flow<RecipeResult<List<MealType>>> = flow {
        emit(RecipeResult.success(getStaticMealCategories()))
    }

    private fun getStaticMealCategories(): List<MealType> {
        val categoryList = mutableListOf<MealType>()
        categoryList.add(MealType(1, "Main course"))
        categoryList.add(MealType(2, "Side dish"))
        categoryList.add(MealType(3, "Bread"))
        categoryList.add(MealType(4, "Marinade"))
        categoryList.add(MealType(5, "Breakfast"))
        categoryList.add(MealType(6, "Fingerfood"))
        categoryList.add(MealType(7, "Dessert"))
        categoryList.add(MealType(8, "Soup"))
        categoryList.add(MealType(9, "Snack"))
        categoryList.add(MealType(10, "Appetizer"))
        categoryList.add(MealType(11, "Beverage"))
        categoryList.add(MealType(12, "Drink"))
        categoryList.add(MealType(13, "Salad"))
        categoryList.add(MealType(14, "Sauce"))
        return categoryList
    }
}