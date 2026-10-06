// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.schedule

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.worker.WorkerScheduler
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultLocalTime
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.toInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import timber.log.Timber

private const val SAVED_MEAL_PLAN_ID = "meal_id"
private const val SAVED_SCHEDULE_DATE_TIME = "saved-meal-schedule-date-time"
private const val SAVED_NOTIFICATION_DATE_TIME = "saved-meal-notification-date-time" // same as preparation time

@HiltViewModel
internal class ScheduleMealViewmodel @Inject constructor(
  private val dispatcher: DispatcherProvider,
  private val mealPlanRepository: MealPlanRepository,
  private val scheduler: WorkerScheduler,
  private val savedState: SavedStateHandle,
) : BaseViewModel(dispatcher) {

  private val _state = MutableStateFlow(ScheduleMealState())
  val state = _state.asStateFlow()

  private val _action: Channel<ScheduleMealAction> = Channel()
  val action = _action.receiveAsFlow()

  init {
    initDate()
    initMealPlan()
  }

  fun onDateSelected(epoch: Long) {
    uiScope.launch {
      val instant = Instant.fromEpochMilliseconds(epoch)
      val selectedDate = instant.defaultLocalDate()
      saveScheduleDate(selectedDate)
      Timber.d("Meal scheduled on date: $selectedDate")
      _state.update { it.copy(scheduleDate = selectedDate.toString()) }
    }
  }

  fun onTimeSet(hour: Int, minute: Int) {
    uiScope.launch(dispatcher.computation) {
      val scheduleDate = getScheduleInstant().defaultLocalDateTime()
      val newScheduleDateTime = LocalDateTime(
        year = scheduleDate.year,
        month = scheduleDate.month.number,
        day = scheduleDate.day,
        hour = hour,
        minute = minute,
      )
      Timber.d("On time set: $hour:$minute")
      val mealPlan = state.value.mealPlan
      val (scheduleTime, preparationTime) = scheduleAndPreparationDateTime(
        newScheduleDateTime,
        mealPlan?.preparationTime,
      )
      Timber.d("On new time set schedule time: $scheduleTime and preparation time: $preparationTime")
      _state.update { currentState ->
        currentState.copy(
          preparationTime = preparationTime.toString(),
          serveTime = scheduleTime.toString(),
        )
      }
    }
  }

  fun submit(needToAddInCalendar: Boolean) {
    Timber.d("Need to add in calendar: $needToAddInCalendar")
    uiScope.launch(dispatcher.computation) {
      val id =
        state.value.mealPlan?.id ?: throw IllegalStateException("How did you came to this state?")
      val scheduleDateTime = getScheduleInstant().defaultLocalDateTime()
//            TODO create notification with alarm for below date time
      val notificationDateTime = getNotificationInstant().defaultLocalDateTime()
//            val scheduleDate = scheduleDateTime.date
//            val scheduleTime = scheduleDateTime.time
//            val localDateTime = LocalDateTime(
//                year = scheduleDate.year,
//                monthNumber = scheduleDate.monthNumber,
//                dayOfMonth = scheduleDate.dayOfMonth,
//                hour = scheduleTime.hour,
//                minute = scheduleTime.minute
//            )
      mealPlanRepository.updateSchedule(id, scheduleDateTime)
      scheduler.scheduleMealWorker(id, notificationDateTime)
      _action.send(ScheduleMealAction.OnMealScheduled)
//            TODO show snack bar meal scheduled
    }
  }

  fun getHourMinute(): Pair<Int, Int> {
    val localTime = getScheduleInstant().defaultLocalTime()
    return Pair(localTime.hour, localTime.minute)
  }

  fun getSelectedDateEpoch() = getScheduleInstant().toEpochMilliseconds()

  private fun initDate() {
    uiScope.launch(dispatcher.computation) {
      val now = defaultNow()
      val dateTime = now.defaultLocalDateTime()
      val date = dateTime.date
      saveScheduleDateTime(dateTime)
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
      val id = savedState.get<Long>(SAVED_MEAL_PLAN_ID)
        ?: throw IllegalArgumentException("No meal to search")
      val mealPlan =
        mealPlanRepository.getMealPlanRecipe(id) ?: throw IllegalStateException("No recipe found")
      withContext(dispatcher.computation) {
        val scheduleDateTime = mealPlan.scheduledFor ?: defaultNow().defaultLocalDateTime()
        val scheduleDate = scheduleDateTime.date
        saveScheduleDateTime(scheduleDateTime)
        val (scheduleTime, preparationTime) = scheduleAndPreparationDateTime(
          mealPlan.scheduledFor,
          mealPlan.preparationTime,
        )
        Timber.d("Meal plan with default schedule time: $scheduleTime and preparation time: $preparationTime")
        _state.update { currentState ->
          currentState.copy(
            scheduleDate = scheduleDate.toString(),
            preparationTime = preparationTime.toString(),
            serveTime = scheduleTime.toString(),
            mealPlan = mealPlan,
          )
        }
      }
    }
  }

  private suspend fun scheduleAndPreparationDateTime(
    mealScheduleDateTime: LocalDateTime? = null,
    mealPreparationTime: Int? = null,
  ): Pair<LocalTime, LocalTime> = withContext(dispatcher.computation) {
    val scheduledDateTime = mealScheduleDateTime ?: getDefaultScheduleDateTime()
    val scheduleTime = scheduledDateTime.time
    saveScheduleTime(scheduleTime)
    Timber.d("Meal schedule at: $scheduledDateTime")
//        TODO: how to handle null or 0 preparation time? maybe show dialog before submitting that there is no preparation time so please be careful while scheduling
    val preparationDelay = (
      (
        mealPreparationTime
          ?: 5
        ) + 10
      ) // additional 10 min delay to arrange ingredients and utensils ;P
    val notificationInstant = scheduledDateTime.toInstant().minus(preparationDelay.minutes)
    saveNotificationDateTime(notificationInstant)
    val preparationDateTime = notificationInstant.defaultLocalDateTime()
    Timber.d("Meal will be notified at: $preparationDateTime")
    val preparationTime = preparationDateTime.time
    Pair(scheduleTime, preparationTime)
  }

  private fun saveNotificationDateTime(instant: Instant) {
    savedState[SAVED_NOTIFICATION_DATE_TIME] = instant.toString()
  }

  private fun getDefaultScheduleDateTime(): LocalDateTime {
    val now = getScheduleInstant().defaultLocalDateTime()
    return LocalDateTime(
      year = now.year,
      month = now.month.number,
      day = now.day,
      hour = 13,
      minute = 0,
    )
  }

  private fun saveScheduleTime(scheduleTime: LocalTime) {
    val oldScheduleDateTime = getScheduleInstant().defaultLocalDate()
    val newDateTime = LocalDateTime(
      year = oldScheduleDateTime.year,
      month = oldScheduleDateTime.month.number,
      day = oldScheduleDateTime.day,
      hour = scheduleTime.hour,
      minute = scheduleTime.minute,
    )
    saveScheduleDateTime(newDateTime)
  }

  private fun saveScheduleDate(date: LocalDate) {
    val oldScheduleDateTime = getScheduleInstant().defaultLocalTime()
    val newDateTime = LocalDateTime(
      year = date.year,
      month = date.month.number,
      day = date.day,
      hour = oldScheduleDateTime.hour,
      minute = oldScheduleDateTime.minute,
    )
    saveScheduleDateTime(newDateTime)
  }

  private fun saveScheduleDateTime(dateTime: LocalDateTime) = savedState.set(SAVED_SCHEDULE_DATE_TIME, dateTime.toInstant().toString())

  private fun getScheduleInstant(): Instant = savedState.get<String?>(SAVED_SCHEDULE_DATE_TIME)?.let {
    Instant.parse(it)
  } ?: throw IllegalStateException("No date time found to schedule recipe")

  private fun getNotificationInstant(): Instant = savedState.get<String?>(SAVED_NOTIFICATION_DATE_TIME)?.let {
    Instant.parse(it)
  } ?: throw IllegalStateException("No notification date time found to receive notification")
}

internal data class ScheduleMealState(
  val scheduleDate: String = "",
  val preparationTime: String = "",
  val serveTime: String = "",
  val mealPlan: MealPlanRecipe? = null,
)

internal sealed interface ScheduleMealAction {
  data object OnMealScheduled : ScheduleMealAction
}
