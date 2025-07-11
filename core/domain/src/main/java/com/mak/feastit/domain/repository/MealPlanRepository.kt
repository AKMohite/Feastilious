// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.model.YumNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

interface MealPlanRepository {
  fun observeTodayMeals(): Flow<List<MealPlanRecipe>>

  fun observeUnscheduledMeals(): Flow<List<MealPlanRecipe>>

  fun observeWeekMeals(
    startDate: Instant,
    endDate: Instant,
  ): Flow<Map<String, List<MealPlanRecipe>>>

  suspend fun toggleMealPLanFor(recipeId: Long): Boolean

  fun hasRecipe(recipeId: Long): Flow<Boolean>

  suspend fun getMealPlanRecipe(id: Long): MealPlanRecipe?

  suspend fun updateSchedule(
    id: Long,
    mealDateTime: LocalDateTime,
  )

  suspend fun getMealPlanNotification(mealId: Long): YumNotification?

  suspend fun getMealPlansForRecipe(recipeId: Long): List<MealPlanRecipe>

  suspend fun remove(id: Long)

  suspend fun repeatMeal(mealPlan: MealPlanRecipe): Long

  suspend fun getUnscheduledMealForRecipe(recipeId: Long): MealPlanRecipe?
}
