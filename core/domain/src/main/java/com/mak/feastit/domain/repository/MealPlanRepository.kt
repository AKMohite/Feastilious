package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.MealPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface MealPlanRepository {
    fun observeTodayMeals(): Flow<List<MealPlan>>
    fun observeUnscheduledMeals(): Flow<List<MealPlan>>
    fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlan>>
}