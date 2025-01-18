package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.MealPlanRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

interface MealPlanRepository {
    fun observeTodayMeals(): Flow<List<MealPlanRecipe>>
    fun observeUnscheduledMeals(): Flow<List<MealPlanRecipe>>
    fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<Map<String, List<MealPlanRecipe>>>
    suspend fun toggleMealPLanFor(recipeId: Long)
    fun hasRecipe(recipeId: Long): Flow<Boolean>
    suspend fun getMealPlanRecipe(recipeId: Long): MealPlanRecipe?
    suspend fun updateSchedule(id: Long, localDateTime: LocalDateTime)
}