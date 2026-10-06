// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.ext

import com.ak.feastit.R
import com.mak.feastit.domain.model.RecipeMealType

internal fun RecipeMealType.getTitleId(): Int = when (this) {
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
