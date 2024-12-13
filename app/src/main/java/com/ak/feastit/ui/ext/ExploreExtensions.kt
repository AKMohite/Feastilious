package com.ak.feastit.ui.ext

import com.ak.feastit.R
import com.mak.feastit.domain.model.Cuisine
import com.mak.feastit.domain.model.DietType
import com.mak.feastit.domain.model.RecipeMealType

internal fun RecipeMealType.getTitleId(): Int {
    return when(this) {
        RecipeMealType.MAIN_COURSE -> R.string.meal_type_main_course
        RecipeMealType.APPETIZER -> R.string.meal_type_appetizer
        RecipeMealType.SIDE_DISH -> R.string.meal_type_side_dish
        RecipeMealType.DESSERT -> R.string.meal_type_dessert
        RecipeMealType.SALAD -> R.string.meal_type_salad
        RecipeMealType.BREAD -> R.string.meal_type_bread
        RecipeMealType.BREAKFAST -> R.string.meal_type_breakfast
        RecipeMealType.SOUP -> R.string.meal_type_soup
        RecipeMealType.BEVERAGE -> R.string.meal_type_beverage
        RecipeMealType.SAUCE -> R.string.meal_type_sauce
        RecipeMealType.MARINADE -> R.string.meal_type_marinade
        RecipeMealType.FINGER_FOOD -> R.string.meal_type_finger_food
        RecipeMealType.SNACK -> R.string.meal_type_snack
        RecipeMealType.DRINK -> R.string.meal_type_drink
    }
}

internal fun Cuisine.getTitleId(): Int {
    return when(this) {
        Cuisine.AFRICAN -> TODO()
        Cuisine.AMERICAN -> TODO()
        Cuisine.BRITISH -> TODO()
        Cuisine.ASIAN -> TODO()
        Cuisine.CAJUN -> TODO()
        Cuisine.CARIBBEAN -> TODO()
        Cuisine.CHINESE -> TODO()
        Cuisine.EASTERN -> TODO()
        Cuisine.EUROPEAN -> TODO()
        Cuisine.FRENCH -> TODO()
        Cuisine.GERMAN -> TODO()
        Cuisine.GREEK -> TODO()
        Cuisine.INDIAN -> TODO()
        Cuisine.IRISH -> TODO()
        Cuisine.ITALIAN -> TODO()
        Cuisine.JAPANESE -> TODO()
        Cuisine.JEWISH -> TODO()
        Cuisine.KOREAN -> TODO()
        Cuisine.LATIN_AMERICAN -> TODO()
        Cuisine.MEDITERRANEAN -> TODO()
        Cuisine.MEXICAN -> TODO()
        Cuisine.MIDDLE_EASTERN -> TODO()
        Cuisine.NORTHERN -> TODO()
        Cuisine.SOUTHERN -> TODO()
        Cuisine.SPANISH -> TODO()
        Cuisine.THAI -> TODO()
        Cuisine.VIETNAMESE -> TODO()
    }
}

internal fun DietType.getTitleId(): Int {
    return when(this) {
        DietType.GLUTEN_FREE -> TODO()
        DietType.KETOGENIC -> TODO()
        DietType.VEGETARIAN -> TODO()
        DietType.PESCETARIAN -> TODO()
        DietType.PALEO -> TODO()
        DietType.PRIMAL -> TODO()
        DietType.LOW_FODMAP -> TODO()
        DietType.WHOLE30 -> TODO()
        DietType.LACTARIAN -> TODO()
        DietType.OVO_VEGETARIAN -> TODO()
        DietType.VEGAN -> TODO()
    }
}