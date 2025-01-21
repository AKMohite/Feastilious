package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.database.entity.custom.MealPlanRecipeEntity
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.daysShift
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.toInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
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

    override suspend fun toggleMealPLanFor(recipeId: Long) = withContext(dispatcher.io) {
        val mealPlan = db.mealPlanDAO().getRecipe(recipeId)
        if (mealPlan.isEmpty()) {
            val new = MealPlanEntity(
                recipeId = recipeId,
                isMade = false,
                plannedFor = null
            )
            db.mealPlanDAO().insert(new)
        } else {
            db.mealPlanDAO().deleteRecipe(recipeId)
            // TODO cancel notifications too
        }
    }

    override suspend fun getMealPlanRecipe(id: Long) = withContext(dispatcher.io) {
        val recipe = db.mealPlanDAO().getMealPlanRecipe(id)
        recipe?.toModel()
    }

    override fun observeTodayMeals(): Flow<List<MealPlanRecipe>> {
        Timber.d("Observe today's meals")
        return db.mealPlanDAO().observeTodayMeals(defaultNow())
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

    override fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<Map<String, List<MealPlanRecipe>>> {
        Timber.d("Observe meals for week in between $startDate and $endDate")
        return db.mealPlanDAO().observeWeekMeals(startDate, endDate)
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

    override suspend fun updateSchedule(id: Long, mealDateTime: LocalDateTime) = withContext(dispatcher.io) {
        val mealPlan = db.mealPlanDAO().getMealPLan(id) ?: throw NullPointerException("No meal found")
        val instant = mealDateTime.toInstant()
        Timber.d("Update meal schedule instant: $instant")
        db.mealPlanDAO().update(mealPlan.copy(plannedFor = instant))
        // TODO schedule notifications
    }
}

private fun MealPlanEntity?.toMealPlanRecipe(): MealPlanRecipe? {
    return this?.let {
        MealPlanRecipe(
            id = it.id,
            recipeId = it.recipeId,
            scheduledFor = plannedFor?.defaultLocalDateTime(),
            isMade = false,
            name = "",
            image = "",
            preparationTime = 0
        )
    }
}

private fun List<MealPlanRecipeEntity>.mapEntitiesToModels(): List<MealPlanRecipe> {
    return this.map { entity -> entity.toModel() }
}

private fun MealPlanRecipeEntity.toModel(): MealPlanRecipe {
    return MealPlanRecipe(
        id = id,
        recipeId = recipeId,
        scheduledFor = scheduledFor?.defaultLocalDateTime(),
        isMade = isMade,
        name = name,
        image = image,
        preparationTime = preparationTime
    )
}
