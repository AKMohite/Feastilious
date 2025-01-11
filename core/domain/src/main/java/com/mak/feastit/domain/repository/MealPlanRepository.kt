package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.MealPlanRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface MealPlanRepository {
    fun observeTodayMeals(): Flow<List<MealPlanRecipe>>
    fun observeUnscheduledMeals(): Flow<List<MealPlanRecipe>>
    fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlanRecipe>>
}