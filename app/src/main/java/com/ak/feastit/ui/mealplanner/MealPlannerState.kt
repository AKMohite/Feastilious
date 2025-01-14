package com.ak.feastit.ui.mealplanner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ak.feastit.R
import com.mak.feastit.domain.model.MealPlanRecipe

data class MealPlannerState(
    val weekRange: String = "",
    val todayRecipes: List<MealPlanRecipe> = emptyList(),
    val weeklyRecipes: Map<String, List<MealPlanRecipe>> = emptyMap(),
    val unscheduledRecipes: List<MealPlanRecipe> = emptyList()
)

internal sealed interface MealPlanAction {
    data class OpenMealPlanBottomSheet(val actions: List<MealPlanRecipeSheetItem>): MealPlanAction
}

internal val defaultActions = listOf(
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_download,
        title = R.string.download_recipe,
        action = MealPlanRecipeAction.DOWNLOAD_RECIPE
    ),
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_share,
        title = R.string.share_recipe,
        action = MealPlanRecipeAction.SHARE_RECIPE
    ),
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_cart_menu,
        title = R.string.add_ingredients_to_shopping_list,
        action = MealPlanRecipeAction.ADD_TO_SHOPPING_LIST
    ),
//    MealPlanRecipeSheetItem(
//        icon = R.drawable.ic_edit,
//        title = R.string.edit_schedule,
//        action = MealPlanRecipeAction.EDIT_SCHEDULE
//    ),
//    MealPlanRecipeSheetItem(
//        icon = R.drawable.ic_edit,
//        title = R.string.set_meal_time,
//        action = MealPlanRecipeAction.SET_MEAL_TIME
//    ),
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_delete,
        title = R.string.recipe_remove_from_meal_plan,
        action = MealPlanRecipeAction.REMOVE_FROM_MEAL_PLAN
    ),
)

internal data class MealPlanRecipeSheetItem(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val action: MealPlanRecipeAction
)


/**
 * if schedule is not present show set schedule else edit schedule
 * if meal time is not present show set meal time else edit meal time
 */
internal enum class MealPlanRecipeAction {
    DOWNLOAD_RECIPE,
    SHARE_RECIPE,
    ADD_TO_SHOPPING_LIST,
    EDIT_SCHEDULE,
    SET_MEAL_TIME,
    REMOVE_FROM_MEAL_PLAN,
}
