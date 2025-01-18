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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import timber.log.Timber
import javax.inject.Inject

private const val SAVED_MEAL_PLAN_ID = "mealId"
private const val SAVED_SCHEDULE_DATE = "meal-schedule-date"
private const val SAVED_SCHEDULE_TIME = "meal-schedule-time"

@HiltViewModel
internal class ScheduleMealViewmodel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val mealPlanRepository: MealPlanRepository,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    private val _state = MutableStateFlow(ScheduleMealState())
    val state = _state.asStateFlow()

    private val _action: Channel<ScheduleMealAction> = Channel()
    val action = _action.receiveAsFlow()

    init {
        initDate()
        initMealPlan()
    }

    fun submit(needToAddInCalendar: Boolean) {
        Timber.d("Need to add in calendar: $needToAddInCalendar")
        uiScope.launch(dispatcher.computation) {
            val id = state.value.mealPlan?.recipeId ?: throw IllegalStateException("How did you came to this state?")
            val scheduleDate = savedState.get<String?>(SAVED_SCHEDULE_DATE)?.let { dateTime ->
                Instant.parse(dateTime).defaultLocalDate()
            } ?: throw IllegalStateException("No schedule date found")
            val scheduleTime = savedState.get<String?>(SAVED_SCHEDULE_TIME)?.let { time ->
                LocalTime.parse(time)
            } ?: throw IllegalStateException("No schedule time found")
            val localDateTime = LocalDateTime(
                year = scheduleDate.year,
                monthNumber = scheduleDate.monthNumber,
                dayOfMonth = scheduleDate.dayOfMonth,
                hour = scheduleTime.hour,
                minute = scheduleTime.minute
            )
            mealPlanRepository.updateSchedule(id, localDateTime)
            _action.send(ScheduleMealAction.OnMealScheduled)
        }
    }

    fun onDateSelected(epoch: Long) {
        uiScope.launch {
            val instant = Instant.fromEpochMilliseconds(epoch)
            saveScheduleDate(instant)
            val selectedDate = instant.defaultLocalDate()
            Timber.d("Meal scheduled on date: $selectedDate")
            _state.update { it.copy(scheduleDate = selectedDate.toString()) }
        }
    }

    private fun initDate() {
        uiScope.launch(dispatcher.computation) {
            val now = defaultNow()
            val date = now.defaultLocalDateTime().date
            saveScheduleDate(now)
            val (scheduleTime, preparationTime) = scheduleAndPreparationDateTime()
            Timber.d("Initialised with schedule time: $scheduleTime and preparation time: $preparationTime")
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
                val (scheduleTime, preparationTime) = scheduleAndPreparationDateTime(mealPlan.scheduledFor?.time, mealPlan.preparationTime)
                Timber.d("Meal plan with default schedule time: $scheduleTime and preparation time: $preparationTime")
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

    private fun scheduleAndPreparationDateTime(mealScheduleTime: LocalTime? = null, mealPreparationTime: Int? = null): Pair<LocalTime, LocalTime> {
        val scheduleTime = mealScheduleTime ?: LocalTime(13, 0)
        saveScheduleTime(scheduleTime)
        val preparationDelay = ((mealPreparationTime ?: 5) + 10) * 60 * 1_000 // 10 min delay to arrange ingredients and utensils ;P
        val preparationTimeMillis = scheduleTime.toMillisecondOfDay().minus(preparationDelay)
        val preparationTime = LocalTime.fromMillisecondOfDay(preparationTimeMillis)
        return Pair(scheduleTime, preparationTime)
    }

    private fun saveScheduleTime(scheduleTime: LocalTime) {
        savedState[SAVED_SCHEDULE_TIME] = scheduleTime.toString()
    }

    fun getSelectedDateEpoch() = savedState.get<String?>(SAVED_SCHEDULE_DATE)?.let { dateTime ->
        Instant.parse(dateTime).toEpochMilliseconds()
    } ?: throw IllegalStateException("No date initialised in init to schedule recipe")

    private fun saveScheduleDate(instant: Instant) {
        savedState[SAVED_SCHEDULE_DATE] = instant.toString()
    }

    fun getHourMinute(): Pair<Int, Int> {
        val localTime = LocalTime.parse(savedState.get<String?>(SAVED_SCHEDULE_TIME)!!)
        return Pair(localTime.hour, localTime.minute)
    }

    fun onTimeSet(hour: Int, minute: Int) {
        uiScope.launch(dispatcher.computation) {
//            val scheduleDate = savedState.get<String?>(SAVED_SCHEDULE_DATE)?.let {
//                Instant.parse(it).defaultLocalDate() } ?: return@launch
//            val newScheduleTime = LocalDateTime(
//                year = scheduleDate.year,
//                monthNumber = scheduleDate.monthNumber,
//                dayOfMonth = scheduleDate.dayOfMonth,
//                hour = hour,
//                minute = minute
//            )
            Timber.d("On time set: $hour:$minute")
            val newScheduleTime = LocalTime(hour = hour, minute = minute)
            val mealPlan = state.value.mealPlan
            val (scheduleTime, preparationTime) = scheduleAndPreparationDateTime(newScheduleTime, mealPlan?.preparationTime)
            Timber.d("On new time set schedule time: $scheduleTime and preparation time: $preparationTime")
            _state.update { currentState ->
                currentState.copy(
                    preparationTime = preparationTime.toString(),
                    serveTime = scheduleTime.toString(),
                )
            }
        }
    }
}

internal data class ScheduleMealState(
    val scheduleDate: String = "",
    val preparationTime: String = "",
    val serveTime: String = "",
    val mealPlan: MealPlanRecipe? = null
)

internal sealed interface ScheduleMealAction {
    data object OnMealScheduled: ScheduleMealAction
}