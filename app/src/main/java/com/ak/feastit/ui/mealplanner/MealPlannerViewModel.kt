package com.ak.feastit.ui.mealplanner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.R
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

private const val SAVED_START_WEEK_DATE = "meal-plan-week-start-date"
private const val SAVED_END_WEEK_DATE = "meal-plan-week-end-date"
private const val SAVED_SELECTED_WEEK_DATE = "meal-plan-week-selected-date"
private const val SAVED_EDIT_RECIPE_ID = "editRecipeId"

@HiltViewModel
internal class MealPlannerViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val dispatcher: DispatcherProvider,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    /**
     * This is selected date that would be helpful to get week range
     */
    private val selectedWeekDate = savedState.getStateFlow<String?>(SAVED_SELECTED_WEEK_DATE, null)

    private val _state = MutableStateFlow(MealPlannerState())
    val state = _state.asStateFlow()

    private val _action: Channel<MealPlanAction> = Channel()
    val action = _action.receiveAsFlow()

    private var editRecipe: MealPlanRecipe? = null

    init {
        observeMealPlans()
        observeWeeklyMeals()
        initWeekDate()
    }

    /**
     * Handle next weeks date range
     */
    fun onNextWeek() {
        uiScope.launch {
            val selectedWeekEndDate = getEndWeekDate()?.let { Instant.parse(it) } ?: return@launch
            val instant = selectedWeekEndDate.plus(1.days)
            saveSelectedDate(instant)
        }
    }

    /**
     * Handle previous weeks date range
     */
    fun onPreviousWeek() {
        uiScope.launch {
            val selectedWeekStartDate = getStartWeekDate()?.let { Instant.parse(it) } ?: return@launch
            val instant = selectedWeekStartDate.minus(1.days)
            saveSelectedDate(instant)
        }
    }

    /**
     * Handle selected date weeks date range
     */
    fun onDateSelected(epoch: Long) {
        uiScope.launch {
            val selectedDate = Instant.fromEpochMilliseconds(epoch)
            saveSelectedDate(selectedDate)
        }
    }

    fun openBottomSheet(recipe: MealPlanRecipe) {
        uiScope.launch {
            savedState[SAVED_EDIT_RECIPE_ID] = recipe.recipeId
            getMealPlanRecipe()
            _action.send(MealPlanAction.OpenMealPlanBottomSheet(recipe.recipeId))
        }
    }

    private fun initWeekDate() {
        val now = defaultNow()
        saveSelectedDate(now)
    }

    private fun saveSelectedDate(instant: Instant) {
        savedState[SAVED_SELECTED_WEEK_DATE] = instant.toString()
    }

    private fun observeMealPlans() {
        combine(
            mealPlanRepository.observeTodayMeals(),
            mealPlanRepository.observeUnscheduledMeals()
        ) { todayMeals, unscheduledMeals ->
            _state.update { currentState ->
                currentState.copy(todayRecipes = todayMeals, unscheduledRecipes = unscheduledMeals)
            }
        }.launchIn(uiScope)
    }

    private fun getMealPlanRecipe() {
        uiScope.launch {
            val recipeId = savedState.get<Long>(SAVED_EDIT_RECIPE_ID) ?: throw IllegalArgumentException("No id found")
            editRecipe = mealPlanRepository.getMealPlanRecipe(recipeId) ?: throw IllegalStateException("No meal plan found for $recipeId")
            val actions = getActions()
            _state.update { currentState -> currentState.copy(menuItems = actions) }
        }
    }

    private suspend fun getActions() = withContext(dispatcher.computation) {
        val menuActions = defaultActions.toMutableList()
        if (editRecipe?.scheduledFor == null) {
            menuActions.add(
                MealPlanRecipeSheetItem(
                    icon = R.drawable.ic_calendar,
                    title = R.string.set_meal_schedule,
                    action = MealPlanSheetMenuAction.SET_SCHEDULE
                )
            )
        } else {
            if (editRecipe?.scheduledFor!!.isToday()) {
                menuActions.add(
                    MealPlanRecipeSheetItem(
                        icon = R.drawable.ic_repeat,
                        title = R.string.repeat_again,
                        action = MealPlanSheetMenuAction.REPEAT_AGAIN
                    )
                )
            }
            menuActions.add(
                MealPlanRecipeSheetItem(
                    icon = R.drawable.ic_calendar_edit,
                    title = R.string.edit_schedule,
                    action = MealPlanSheetMenuAction.EDIT_SCHEDULE
                )
            )
        }
        menuActions.toList().sortedBy { it.action.ordinal }
    }

//    TODO why combine is not working with flatmapMerge? and flatmapMerge does not work with .shareIn()
    private fun observeWeeklyMeals() {
        selectedWeekDate
            .filterNotNull()
            .map { selectedDate ->
                val range = getWeekRange(selectedDate)
                val start = range.first.defaultLocalDate()
                val end = range.second.defaultLocalDate()
                val weekRange = "${start.dayOfMonth} ${start.month/*.toString().take(3)*/} - ${end.dayOfMonth} ${end.month}"
                _state.update { it.copy(weekRange = weekRange) }
                range
            }.flowOn(dispatcher.computation)
            .debounce(500)
            .flatMapMerge { (start, end) ->
                mealPlanRepository.observeWeekMeals(start, end)
            }.onEach { weeklyMeals ->
                _state.update { currentState ->
                    currentState.copy(weeklyRecipes = weeklyMeals)
                }
            }.launchIn(uiScope)
    }

    /**
     * @return selected date week, i.e, Starting Monday date and ending Sunday date
     */
    private fun getWeekRange(selectedDate: String): Pair<Instant, Instant> {
        val instant = Instant.parse(selectedDate)
        val nowDateTime = instant.defaultLocalDateTime()
        val dayOfWeek = DayOfWeek.entries.indexOf(nowDateTime.dayOfWeek)
        val startWeek = instant.minus(dayOfWeek.days)
        val lastIndex = DayOfWeek.entries.count() - 1 - dayOfWeek
        val lastWeek = instant.plus(lastIndex.days)
        savedState[SAVED_START_WEEK_DATE] = startWeek.toString()
        savedState[SAVED_END_WEEK_DATE] = lastWeek.toString()
        return Pair(startWeek, lastWeek)
    }

    private fun getStartWeekDate(): String? = savedState[SAVED_START_WEEK_DATE]
    private fun getEndWeekDate(): String? = savedState[SAVED_END_WEEK_DATE]
    fun getSelectedDateEpoch(): Long? = savedState.get<String?>(SAVED_SELECTED_WEEK_DATE)?.let { selected ->
        Instant.parse(selected).toEpochMilliseconds()
    }

    fun onMealPlanMenuClick(item: MealPlanRecipeSheetItem) {
        val recipe = editRecipe ?: throw IllegalStateException("No recipe found for meal plan edit")
        when(item.action) {
            MealPlanSheetMenuAction.ADD_TO_SHOPPING_LIST -> {}
            MealPlanSheetMenuAction.REMOVE_FROM_MEAL_PLAN -> {}
            else -> {
                uiScope.launch {
                    _action.send(MealPlanAction.OnMenuClick(item.action, recipe))
                }
            }
        }
    }

    fun getEditRecipe(): Long {
        return editRecipe?.recipeId ?: throw IllegalStateException("No recipe found")
    }


}



internal val defaultActions = listOf(
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_download,
        title = R.string.download_recipe,
        action = MealPlanSheetMenuAction.DOWNLOAD_RECIPE
    ),
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_share,
        title = R.string.share_recipe,
        action = MealPlanSheetMenuAction.SHARE_RECIPE
    ),
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_cart_menu,
        title = R.string.add_ingredients_to_shopping_list,
        action = MealPlanSheetMenuAction.ADD_TO_SHOPPING_LIST
    ),
    MealPlanRecipeSheetItem(
        icon = R.drawable.ic_delete,
        title = R.string.recipe_remove_from_meal_plan,
        action = MealPlanSheetMenuAction.REMOVE_FROM_MEAL_PLAN
    )
)

internal data class MealPlanRecipeSheetItem(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val action: MealPlanSheetMenuAction
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
internal enum class MealPlanSheetMenuAction {
    DOWNLOAD_RECIPE, // download recipe
    SHARE_RECIPE, // share recipe with url?
    ADD_TO_SHOPPING_LIST, // add recipe ingredients to cart
    REPEAT_AGAIN, // repeat recipe for next week with today + 7.days
    SET_SCHEDULE, // if we have don't have schedule date set it
    EDIT_SCHEDULE, // if we have schedule date edit date
    REMOVE_FROM_MEAL_PLAN, // remove from meal plan
}
