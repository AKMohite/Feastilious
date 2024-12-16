package com.mak.feastit.domain.model

import java.util.Locale

data class MealType (
    val id: Int,
    val categoryName: String
)

enum class RecipeMealType {
    MAIN_COURSE,
    APPETIZER,
    SIDE_DISH,
    DESSERT,
    SALAD,
    BREAD,
    BREAKFAST,
    SOUP,
    BEVERAGE,
    SAUCE,
    MARINADE,
    FINGER_FOOD,
    SNACK,
    DRINK;

    fun getTitle(): String {
        return this.name.replace("_", " ")
            .replace(" chips", "", ignoreCase = true)
            .lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}