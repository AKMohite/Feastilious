package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.domain.model.MealPlan
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

internal class RealMealPlanRepository @Inject constructor(
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): MealPlanRepository {

    override fun observeTodayMeals(): Flow<List<MealPlan>> {
        return db.mealPlanDAO().observeTodayMeals(Clock.System.now())
            .flowOn(dispatcher.io)
            .map { entities ->
                entities.mapEntitiesToModels()
            }.flowOn(dispatcher.computation)
    }

    override fun observeUnscheduledMeals(): Flow<List<MealPlan>> {
        return db.mealPlanDAO().observeUnscheduledMeals()
            .flowOn(dispatcher.io)
            .map { entities ->
                entities.mapEntitiesToModels()
            }.flowOn(dispatcher.computation)
    }

    override fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlan>> {
        return db.mealPlanDAO().observeWeekMeals(startDate, endDate)
            .flowOn(dispatcher.io)
            .map { entities ->
                entities.mapEntitiesToModels()
            }.flowOn(dispatcher.computation)
    }
}

private fun List<MealPlanEntity>.mapEntitiesToModels(): List<MealPlan> {
    return this.map { entity -> entity.toModel() }
}

private fun MealPlanEntity.toModel(): MealPlan {
    return MealPlan(
        recipeId = id,
        scheduledFor = plannedFor?.toLocalDateTime(TimeZone.currentSystemDefault()),
        isMade = isMade
    )
}
