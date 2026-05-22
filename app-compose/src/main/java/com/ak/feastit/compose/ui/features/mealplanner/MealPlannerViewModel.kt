// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.mealplanner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.compose.R
import com.ak.feastit.compose.base.BaseViewModel
import com.ak.feastit.compose.worker.WorkerScheduler
import com.mak.feastit.domain.model.DayMealPlan
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.model.WeekMealPlanSection
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
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
import timber.log.Timber

private const val SAVED_START_WEEK_DATE = "saved-meal-plan-week-start-date"
private const val SAVED_END_WEEK_DATE = "saved-meal-plan-week-end-date"
private const val SAVED_SELECTED_WEEK_DATE = "saved-meal-plan-week-selected-date"
private const val SAVED_EDIT_MEAL_PLAN_ID = "saved-meal-plan-id"

@HiltViewModel
internal class MealPlannerViewModel @Inject constructor(
  private val mealPlanRepository: MealPlanRepository,
  private val scheduler: WorkerScheduler,
  private val dispatcher: DispatcherProvider,
  private val savedState: SavedStateHandle,
) : BaseViewModel(dispatcher) {

  private val selectedWeekDate = savedState.getStateFlow<String?>(SAVED_SELECTED_WEEK_DATE, null)

  private val _state = MutableStateFlow(MealPlannerState())
  val state = _state.asStateFlow()

  private val _action: Channel<MealPlanAction> = Channel()
  val action = _action.receiveAsFlow()

  private var editMealPlan: MealPlanRecipe? = null

  init {
    observeMealPlans()
    observeWeeklyMeals()
    initWeekDate()
  }

  fun onNextWeek() {
    uiScope.launch {
      val selectedWeekEndDate = getEndWeekDate()?.let { Instant.parse(it) } ?: return@launch
      val instant = selectedWeekEndDate.plus(1.days)
      saveSelectedDate(instant)
    }
  }

  fun onPreviousWeek() {
    uiScope.launch {
      val selectedWeekStartDate = getStartWeekDate()?.let { Instant.parse(it) } ?: return@launch
      val instant = selectedWeekStartDate.minus(1.days)
      saveSelectedDate(instant)
    }
  }

  fun onDateSelected(epoch: Long) {
    uiScope.launch {
      val selectedDate = Instant.fromEpochMilliseconds(epoch)
      saveSelectedDate(selectedDate)
    }
  }

  fun openBottomSheet(mealPlan: MealPlanRecipe) {
    uiScope.launch {
      savedState[SAVED_EDIT_MEAL_PLAN_ID] = mealPlan.id
      getMealPlanRecipe()
      _action.send(MealPlanAction.OpenMealPlanBottomSheet(mealPlan.id))
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
      mealPlanRepository.observeUnscheduledMeals(),
    ) { todayMeals, unscheduledMeals ->
      _state.update { currentState ->
        currentState.copy(todayRecipes = todayMeals, unscheduledRecipes = unscheduledMeals)
      }
    }.launchIn(uiScope)
  }

  private fun getMealPlanRecipe() {
    uiScope.launch {
      val mealPlanId = savedState.get<Long>(SAVED_EDIT_MEAL_PLAN_ID)
        ?: throw IllegalArgumentException("No id found")
      editMealPlan = mealPlanRepository.getMealPlanRecipe(mealPlanId)
        ?: throw IllegalStateException("No meal plan found for $mealPlanId")
      val actions = getActions()
      _state.update { currentState -> currentState.copy(menuItems = actions) }
    }
  }

  private suspend fun getActions() = withContext(dispatcher.computation) {
    val menuActions = defaultActions.toMutableList()
    if (editMealPlan?.scheduledFor == null) {
      menuActions.add(
        MealPlanRecipeSheetItem(
          icon = R.drawable.ic_calendar,
          title = R.string.set_meal_schedule,
          action = MealPlanSheetMenuAction.RESCHEDULE, // Changed from SET_SCHEDULE to align with local enum
        ),
      )
    } else {
      if (editMealPlan?.scheduledFor!!.isToday()) {
        menuActions.add(
          MealPlanRecipeSheetItem(
            icon = R.drawable.ic_repeat,
            title = R.string.repeat_again,
            action = MealPlanSheetMenuAction.REPEAT, // Changed from REPEAT_AGAIN
          ),
        )
      }
      menuActions.add(
        MealPlanRecipeSheetItem(
          icon = R.drawable.ic_calendar_edit,
          title = R.string.edit_schedule,
          action = MealPlanSheetMenuAction.RESCHEDULE, // Changed from EDIT_SCHEDULE
        ),
      )
    }
    menuActions.toList().sortedBy { it.action.ordinal }
  }

  private fun observeWeeklyMeals() {
    selectedWeekDate
      .filterNotNull()
      .map { selectedDate ->
        val range = getWeekRange(selectedDate)
        val start = range.first.defaultLocalDate()
        val end = range.second.defaultLocalDate()
        val weekRange = "${start.day} ${
          start.month.toString().take(3)
        } - ${end.day} ${end.month.toString().take(3)}"
        _state.update { it.copy(weekRange = weekRange) }
        range
      }.flowOn(dispatcher.computation)
      .debounce(500)
      .flatMapMerge { (start, end) ->
        mealPlanRepository.observeWeekMeals(start, end)
      }.map { mealPlans ->
        val sections = mutableListOf<WeekMealPlanSection>()
        for (plan in mealPlans) {
          val noOfMeal = plan.value.count()
          val mealCount = if (noOfMeal == 0) {
            ""
          } else if (noOfMeal > 9) {
            "9+"
          } else {
            "$noOfMeal"
          }
          val dayMealPlan = DayMealPlan(day = plan.key, noOfMeal = mealCount)
          val header = WeekMealPlanSection.DayHeader(dayMealPlan)
          sections.add(header)
          plan.value.map { meal ->
            sections.add(WeekMealPlanSection.MealRecipe(meal = meal))
          }
        }
        sections.toList()
      }.flowOn(dispatcher.computation).onEach { weeklyMeals ->
        _state.update { currentState ->
          currentState.copy(weeklySections = weeklyMeals)
        }
      }.launchIn(uiScope)
  }

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
    val mealPlan = editMealPlan ?: throw IllegalStateException("No recipe found for meal plan edit")
    when (item.action) {
      MealPlanSheetMenuAction.DOWNLOAD -> {} // TODO
      MealPlanSheetMenuAction.REMOVE -> removeFromMealPlan(mealPlan)
      MealPlanSheetMenuAction.REPEAT -> {
        uiScope.launch {
          val plan = mealPlanRepository.getUnscheduledMealForRecipe(mealPlan.recipeId)
          plan?.let { Timber.d("Repeat again with rescheduled meal: $it") }
          val mealId = plan?.id ?: mealPlanRepository.repeatMeal(mealPlan)
          _action.send(MealPlanAction.OnMealRepeat(mealId))
        }
      }
      else -> {
        uiScope.launch {
          _action.send(MealPlanAction.OnMenuClick(item.action, mealPlan))
        }
      }
    }
  }

  private fun removeFromMealPlan(mealPlan: MealPlanRecipe) {
    uiScope.launch {
      mealPlanRepository.remove(mealPlan.id)
      scheduler.cancelMealWorker(mealPlan.id)
      // notification.cancel(mealPlan.toNotification()) // TODO
    }
  }

  fun getEditRecipe(): Long {
    return editMealPlan?.recipeId ?: throw IllegalStateException("No recipe found")
  }
}

internal val defaultActions = listOf(
  MealPlanRecipeSheetItem(
    icon = R.drawable.ic_download,
    title = R.string.download_recipe,
    action = MealPlanSheetMenuAction.DOWNLOAD,
  ),
  MealPlanRecipeSheetItem(
    icon = R.drawable.ic_share,
    title = R.string.share_recipe,
    action = MealPlanSheetMenuAction.SHARE,
  ),
  MealPlanRecipeSheetItem(
    icon = R.drawable.ic_cart_menu,
    title = R.string.add_ingredients_to_shopping_list,
    action = MealPlanSheetMenuAction.REPEAT, // Placeholder
  ),
  MealPlanRecipeSheetItem(
    icon = R.drawable.ic_delete,
    title = R.string.recipe_remove_from_meal_plan,
    action = MealPlanSheetMenuAction.REMOVE,
  ),
)
