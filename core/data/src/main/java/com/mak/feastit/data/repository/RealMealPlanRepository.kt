// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.database.entity.custom.MealPlanRecipeEntity
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.model.YumNotification
import com.mak.feastit.domain.model.YumNotificationChannel
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.daysShift
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.toInstant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import timber.log.Timber

internal class RealMealPlanRepository
@Inject
constructor(
  private val db: FeastDB,
  private val dispatcher: DispatcherProvider,
) : MealPlanRepository {
  override fun hasRecipe(recipeId: Long): Flow<Boolean> = db
    .mealPlanDAO()
    .hasRecipe(recipeId)
    .map { id ->
      id != null
    }.flowOn(dispatcher.io)

  override suspend fun toggleMealPLanFor(recipeId: Long): Boolean = withContext(dispatcher.io) {
    val mealPlan = db.mealPlanDAO().getRecipe(recipeId)
    if (mealPlan.isEmpty()) {
      val new =
        MealPlanEntity(
          recipeId = recipeId,
          isMade = false,
          plannedFor = null,
        )
      db.mealPlanDAO().insert(new)
      true
    } else {
      db.mealPlanDAO().deleteRecipe(recipeId)
      false
    }
  }

  override suspend fun getMealPlanRecipe(id: Long) = withContext(dispatcher.io) {
    val recipe = db.mealPlanDAO().getMealPlanRecipe(id)
    recipe?.toModel()
  }

  override fun observeTodayMeals(): Flow<List<MealPlanRecipe>> {
    Timber.d("Observe today's meals")
    return db
      .mealPlanDAO()
      .observeTodayMeals(defaultNow())
      .flowOn(dispatcher.io)
      .map { entities ->
        entities.mapEntitiesToModels()
      }.flowOn(dispatcher.computation)
  }

  override fun observeUnscheduledMeals(): Flow<List<MealPlanRecipe>> {
    Timber.d("Observe unscheduled meals")
    return db
      .mealPlanDAO()
      .observeUnscheduledMeals()
      .flowOn(dispatcher.io)
      .map { entities ->
        entities.mapEntitiesToModels()
      }.flowOn(dispatcher.computation)
  }

  override fun observeWeekMeals(
    startDate: Instant,
    endDate: Instant,
  ): Flow<Map<String, List<MealPlanRecipe>>> {
    Timber.d("Observe meals for week in between $startDate and $endDate")
    return db
      .mealPlanDAO()
      .observeWeekMeals(startDate, endDate)
      .flowOn(dispatcher.io)
      .map { entities ->
        val weeklyMealPlans = mutableMapOf<String, List<MealPlanRecipe>>()
        val firstWeekDay = startDate.defaultLocalDate()
        val recipes = entities.mapEntitiesToModels().groupBy { it.scheduledFor?.date }
        for (i in 0 until DayOfWeek.entries.toTypedArray().count()) {
          val date = firstWeekDay.daysShift(i)
          weeklyMealPlans[date.dayOfWeek.name] = recipes[date] ?: emptyList()
        }
        weeklyMealPlans
      }.flowOn(dispatcher.computation)
  }

  override suspend fun updateSchedule(
    id: Long,
    mealDateTime: LocalDateTime,
  ) = withContext(dispatcher.io) {
    val mealPlan = db.mealPlanDAO().getMealPLan(id) ?: throw NullPointerException("No meal found")
    val instant = mealDateTime.toInstant()
    Timber.d("Update meal schedule instant: $instant")
    db.mealPlanDAO().update(mealPlan.copy(plannedFor = instant))
  }

  override suspend fun getMealPlanNotification(mealId: Long): YumNotification? = withContext(dispatcher.io) {
    val mealPlan =
      db.mealPlanDAO().getMealPLan(mealId) ?: throw NullPointerException("No meal found")
    mealPlan.toNotification()
  }

  override suspend fun getMealPlansForRecipe(recipeId: Long): List<MealPlanRecipe> = withContext(dispatcher.io) {
    val mealPlans = db.mealPlanDAO().getRecipe(recipeId)
    return@withContext mealPlans.mapNotNull { it.toMealPlanRecipe() }
  }

  override suspend fun remove(id: Long) = withContext(dispatcher.io) {
    Timber.d("Remove meal from plan $id")
    db.mealPlanDAO().deletePlan(id)
  }

  override suspend fun repeatMeal(mealPlan: MealPlanRecipe): Long = withContext(dispatcher.io) {
    Timber.d("Create repeat again recipe for other time")
    val entity = MealPlanEntity(recipeId = mealPlan.recipeId, plannedFor = null, isMade = false)
    db.mealPlanDAO().insert(entity)
  }

  override suspend fun getUnscheduledMealForRecipe(recipeId: Long): MealPlanRecipe? = withContext(dispatcher.io) {
    val entity = db.mealPlanDAO().getUnscheduledMeal(recipeId)
    entity?.toMealPlanRecipe()
  }
}

private fun MealPlanEntity.toNotification(): YumNotification = YumNotification(
  id = "meal-plan-$id-$recipeId",
  title = "TODO",
  message = "TODO",
  channel = YumNotificationChannel.MEAL_PLANNING,
  dateTime = plannedFor!!,
  deeplinkUrl = null,
  image = null,
)

private fun MealPlanEntity?.toMealPlanRecipe(): MealPlanRecipe? = this?.let {
  MealPlanRecipe(
    id = it.id,
    recipeId = it.recipeId,
    scheduledFor = plannedFor?.defaultLocalDateTime(),
    isMade = false,
    name = "",
    image = "",
    preparationTime = 0,
  )
}

internal fun List<MealPlanRecipeEntity>.mapEntitiesToModels(): List<MealPlanRecipe> = this.map { entity -> entity.toModel() }

private fun MealPlanRecipeEntity.toModel(): MealPlanRecipe = MealPlanRecipe(
  id = id,
  recipeId = recipeId,
  scheduledFor = scheduledFor?.defaultLocalDateTime(),
  isMade = isMade,
  name = name,
  image = image,
  preparationTime = preparationTime,
)
