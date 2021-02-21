package com.ak.data.category

import com.ak.domain.category.CategoryRepository
import com.ak.domain.category.RecipeCategory
import com.ak.domain.utils.RecipeResult

class CategoryRepositoryImpl : CategoryRepository {

    override fun getFoodCategories(): RecipeResult<List<RecipeCategory>> {
        return RecipeResult.Success(getStaticMealCategories())
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