package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.database.entity.custom.MealPlanRecipeEntity
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject

internal class RealMealPlanRepository @Inject constructor(
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): MealPlanRepository {

    override fun hasRecipe(recipeId: Long): Flow<Boolean> {
        return db.mealPlanDAO().hasRecipe(recipeId)
            .map { id ->
                id != null
            }.flowOn(dispatcher.io)
    }

    //    TODO cancel notifications too
    override suspend fun toggleMealPLanFor(recipeId: Long) = withContext(dispatcher.io) {
        val mealPlan = db.mealPlanDAO().getRecipe(recipeId)
        if (mealPlan == null) {
            val new = MealPlanEntity(
                id = recipeId,
                isMade = false,
                plannedFor = null
            )
            db.mealPlanDAO().insert(new)
        } else {
            db.mealPlanDAO().delete(mealPlan)
        }
    }

    override fun observeTodayMeals(): Flow<List<MealPlanRecipe>> {
        Timber.d("Observe today's meals")
        return db.mealPlanDAO().observeTodayMeals(Clock.System.now())
            .flowOn(dispatcher.io)
            .map { entities ->
                entities.mapEntitiesToModels()
            }.flowOn(dispatcher.computation)
    }

    override fun observeUnscheduledMeals(): Flow<List<MealPlanRecipe>> {
        Timber.d("Observe unscheduled meals")
        return db.mealPlanDAO().observeUnscheduledMeals()
            .flowOn(dispatcher.io)
            .map { entities ->
                entities.mapEntitiesToModels()
            }.flowOn(dispatcher.computation)
    }

    override fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlanRecipe>> {
        Timber.d("Observe meals for week in between $startDate and $endDate")
        return db.mealPlanDAO().observeWeekMeals(startDate, endDate)
            .flowOn(dispatcher.io)
            .map { entities ->
                entities.mapEntitiesToModels()
            }.flowOn(dispatcher.computation)
    }
}

private fun List<MealPlanRecipeEntity>.mapEntitiesToModels(): List<MealPlanRecipe> {
    return this.map { entity -> entity.toModel() }
}

private fun MealPlanRecipeEntity.toModel(): MealPlanRecipe {
    return MealPlanRecipe(
        recipeId = id,
        scheduledFor = scheduledFor?.toLocalDateTime(TimeZone.currentSystemDefault()),
        isMade = isMade,
        name = name,
        image = image
    )
}
