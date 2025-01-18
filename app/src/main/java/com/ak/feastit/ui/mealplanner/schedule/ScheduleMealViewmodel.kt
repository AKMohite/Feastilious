package com.ak.feastit.ui.mealplanner.schedule

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.toInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import javax.inject.Inject

private const val SAVED_MEAL_PLAN_ID = "mealId"
private const val SAVED_SCHEDULE_DATE = "meal-schedule-date"

@HiltViewModel
internal class ScheduleMealViewmodel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val mealPlanRepository: MealPlanRepository,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    private val _state = MutableStateFlow(ScheduleMealState())
    val state = _state.asStateFlow()

    init {
        initDate()
        initMealPlan()
    }

    private fun initDate() {
        uiScope.launch(dispatcher.computation) {
            val now = defaultNow()
            val date = now.defaultLocalDateTime().date
            saveScheduleDate(now)
            val scheduleTime = LocalTime(13, 0)
//            15 min delay to arrange ingredients and utensils
            val preparationDelay = 15 * 60 * 1_000
            val preparationTimeMillis = scheduleTime.toMillisecondOfDay().minus(preparationDelay)
            val preparationTime = LocalTime.fromMillisecondOfDay(preparationTimeMillis)
            _state.update { currentState ->
                currentState.copy(
                    scheduleDate = date.toString(),
                    preparationTime = preparationTime.toString(),
                    serveTime = scheduleTime.toString(),
                )
            }
        }
    }

    private fun initMealPlan() {
        uiScope.launch {
            val id = savedState.get<Long>(SAVED_MEAL_PLAN_ID) ?: throw IllegalArgumentException("No id found")
            val mealPlan = mealPlanRepository.getMealPlanRecipe(id) ?: throw IllegalStateException("No recipe found")
            withContext(dispatcher.computation) {
                val scheduleDateTime = mealPlan.scheduledFor ?: defaultNow().defaultLocalDateTime()
                val scheduleDate = scheduleDateTime.date
                saveScheduleDate(scheduleDateTime.toInstant())
                val scheduleTime = mealPlan.scheduledFor?.time ?: LocalTime(13, 0)
                val preparationDelay = ((mealPlan.preparationTime ?: 5) + 10) * 60 * 1_000 // 10 min delay to arrange ingredients and utensils ;P
                val preparationTimeMillis = scheduleTime.toMillisecondOfDay().minus(preparationDelay)
                val preparationTime = LocalTime.fromMillisecondOfDay(preparationTimeMillis)
                _state.update { currentState ->
                    currentState.copy(
                        scheduleDate = scheduleDate.toString(),
                        preparationTime = preparationTime.toString(),
                        serveTime = scheduleTime.toString(),
                        mealPlan = mealPlan
                    )
                }
            }
        }
    }

    fun submit(needToAddInCalendar: Boolean) {
//        TODO add remove reminder from calendar
    }

    fun getSelectedDateEpoch() = savedState.get<String?>(SAVED_SCHEDULE_DATE)?.let { dateTime ->
        Instant.parse(dateTime).toEpochMilliseconds()
    } ?: throw IllegalStateException("No date initialised in init to schedule recipe")

    fun onDateSelected(epoch: Long) {
        uiScope.launch {
            val instant = Instant.fromEpochMilliseconds(epoch)
            saveScheduleDate(instant)
            val selectedDate = instant.defaultLocalDate()
            _state.update { it.copy(scheduleDate = selectedDate.toString()) }
        }
    }

    private fun saveScheduleDate(instant: Instant) {
        savedState[SAVED_SCHEDULE_DATE] = instant.toString()
    }
}

internal data class ScheduleMealState(
    val scheduleDate: String = "",
    val preparationTime: String = "",
    val serveTime: String = "",
    val mealPlan: MealPlanRecipe? = null
)