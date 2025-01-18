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

    override suspend fun getMealPlanRecipe(recipeId: Long) = withContext(dispatcher.io) {
        val recipe = db.mealPlanDAO().getMealPlanRecipe(recipeId)
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

    override suspend fun updateSchedule(id: Long, localDateTime: LocalDateTime) = withContext(dispatcher.io) {
        val mealPlan = db.mealPlanDAO().getRecipe(id) ?: throw NullPointerException("No meal found")
        val instant = localDateTime.toInstant()
        Timber.d("Update schedule instant: $instant")
        db.mealPlanDAO().update(mealPlan.copy(plannedFor = instant))
    }
}

private fun MealPlanEntity?.toMealPlanRecipe(): MealPlanRecipe? {
    return this?.let {
        MealPlanRecipe(
            recipeId = it.id,
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
        recipeId = id,
        scheduledFor = scheduledFor?.defaultLocalDateTime(),
        isMade = isMade,
        name = name,
        image = image,
        preparationTime = preparationTime
    )
}
