package com.ak.feastit.ui.mealplanner.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.R
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val SAVED_EDIT_RECIPE_ID = "editRecipeId"

@HiltViewModel
internal class MealPlannerSheetViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val dispatcher: DispatcherProvider,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    private lateinit var editRecipe: MealPlanRecipe
    private val _state = MutableStateFlow((emptyList<MealPlanRecipeSheetItem>()))
    val state = _state.asStateFlow()

    init {
        getMealPlanRecipe()
    }

    private fun getMealPlanRecipe() {
        uiScope.launch {
            val recipeId = savedState.get<Long>(SAVED_EDIT_RECIPE_ID) ?: throw IllegalArgumentException("No id found")
            editRecipe = mealPlanRepository.getMealPlanRecipe(recipeId) ?: throw IllegalStateException("No meal plan found for $recipeId")
            val actions = getActions()
            _state.update { actions }
        }
    }

    private suspend fun getActions() = withContext(dispatcher.computation) {
        val menuActions = defaultActions.toMutableList()
        if (editRecipe.scheduledFor == null) {
            menuActions.add(
                MealPlanRecipeSheetItem(
                    icon = R.drawable.ic_edit,
                    title = R.string.set_meal_schedule,
                    action = MealPlanRecipeAction.SET_SCHEDULE
                )
            )
        } else {
            menuActions.add(
                MealPlanRecipeSheetItem(
                    icon = R.drawable.ic_repeat,
                    title = R.string.repeat_next_week,
                    action = MealPlanRecipeAction.REPEAT_NEXT_WEEK
                )
            )
            menuActions.add(
                MealPlanRecipeSheetItem(
                    icon = R.drawable.ic_edit,
                    title = R.string.edit_schedule,
                    action = MealPlanRecipeAction.EDIT_SCHEDULE
                )
            )
            menuActions.add(
                MealPlanRecipeSheetItem(
                    icon = R.drawable.ic_edit,
                    title = R.string.set_meal_time,
                    action = MealPlanRecipeAction.SET_MEAL_TIME
                )
            )
        }
        menuActions.toList().sortedBy { it.action.ordinal }
    }
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
) {
    fun isSameAs(other: MealPlanRecipeSheetItem): Boolean {
        return action == other.action &&
                title == other.title
    }
}


/**
 * if schedule is not present show set schedule else edit schedule
 * if meal time is not present show set meal time else edit meal time
 */
internal enum class MealPlanRecipeAction {
    DOWNLOAD_RECIPE, // download recipe
    SHARE_RECIPE, // share recipe with url?
    ADD_TO_SHOPPING_LIST, // add recipe ingredients to cart
    REPEAT_NEXT_WEEK, // repeat recipe for next week with today + 7.days
    SET_SCHEDULE, // if we have don't have schedule date set it
    EDIT_SCHEDULE, // if we have schedule date edit date
    SET_MEAL_TIME, // if we have don't have meal time set it
    REMOVE_FROM_MEAL_PLAN, // remove from meal plan
}
