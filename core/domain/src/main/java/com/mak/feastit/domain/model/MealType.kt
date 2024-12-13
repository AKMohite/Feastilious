package com.mak.feastit.domain.model

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
    DRINK
}