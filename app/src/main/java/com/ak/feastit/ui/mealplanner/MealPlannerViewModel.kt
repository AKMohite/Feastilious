package com.ak.feastit.ui.mealplanner

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.daysShift
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

private const val SAVED_START_WEEK_DATE = "meal-plan-week-start-date"
private const val SAVED_END_WEEK_DATE = "meal-plan-week-end-date"

@HiltViewModel
internal class MealPlannerViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    dispatcher: DispatcherProvider,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    private val startWeekDate: StateFlow<String?> = savedState.getStateFlow(SAVED_START_WEEK_DATE, null)
    private val endWeekDate: StateFlow<String?> = savedState.getStateFlow(SAVED_END_WEEK_DATE, null)

    init {
        initWeekDate()
        observeMealPlans()
    }

    /**
     * Handle next weeks date range
     */
    fun onNextWeek() {
        uiScope.launch {
            val selectedWeekEndDate = endWeekDate.value?.let { Instant.parse(it) } ?: return@launch
            val startDate = selectedWeekEndDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.daysShift(1)
            val endDate = startDate.daysShift(DayOfWeek.entries.count() - 1)

            savedState[SAVED_START_WEEK_DATE] = startDate
            savedState[SAVED_END_WEEK_DATE] = endDate
        }
    }

    /**
     * Handle previous weeks date range
     */
    fun onPreviousWeek() {
        uiScope.launch {
            val selectedWeekStartDate = startWeekDate.value?.let { Instant.parse(it) } ?: return@launch
            val endDate = selectedWeekStartDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.daysShift(-1)
            val startDate = endDate.daysShift(DayOfWeek.entries.count() - 1)

            savedState[SAVED_START_WEEK_DATE] = startDate
            savedState[SAVED_END_WEEK_DATE] = endDate
        }
    }

    /**
     * Handle selected date weeks date range
     */
    fun onDateSelected() {
//        TODO handle the date range from savedState and handle multiple week change using date picker
        uiScope.launch {
//            TODO the selected date need to be passed from UI
            val selectedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            val firstWeekDate = selectedDate.daysShift(-DayOfWeek.entries.indexOf(selectedDate.dayOfWeek))
            val endWeekDate = firstWeekDate.daysShift(DayOfWeek.entries.size - 1)

            savedState[SAVED_START_WEEK_DATE] = firstWeekDate
            savedState[SAVED_END_WEEK_DATE] = endWeekDate
        }
    }

    private fun initWeekDate() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val firstWeekDate = today.daysShift(-DayOfWeek.entries.indexOf(today.dayOfWeek))
        val endWeekDate = firstWeekDate.daysShift(DayOfWeek.entries.size - 1)

        savedState[SAVED_START_WEEK_DATE] = firstWeekDate
        savedState[SAVED_END_WEEK_DATE] = endWeekDate
    }

    private fun observeMealPlans() {
        combine(
            mealPlanRepository.observeTodayMeals(),
            getWeekMealPlan(),
            mealPlanRepository.observeUnscheduledMeals()
        ) { todayMeals, weekMeals, unscheduledMeals ->

        }.shareIn(uiScope, SharingStarted.Lazily, 5_000)
    }

    private fun getWeekMealPlan(): Flow<List<MealPlanRecipe>> {
        return combine(
            startWeekDate,
            endWeekDate
        ) { start, end ->
            if (start == null || end == null) return@combine null
            Pair(Instant.parse(start), Instant.parse(end))
        }.filterNotNull().flatMapMerge { (start, end) ->
            mealPlanRepository.observeWeekMeals(start, end)
        }
    }
}