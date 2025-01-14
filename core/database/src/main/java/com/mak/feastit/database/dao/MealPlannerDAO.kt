package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.database.entity.custom.MealPlanRecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

private const val MEAL_PLAN_QUERY = "SELECT mp.id, mp.planned_for, mp.is_made, r.name, r.img, r.ready_in_mins AS preparation_time FROM meal_planner mp INNER JOIN recipes r ON mp.id = r.id"

@Dao
interface MealPlannerDAO: BaseDAO<MealPlanEntity> {

    @Query("$MEAL_PLAN_QUERY WHERE mp.planned_for = date(:today) ORDER BY mp.planned_for")
    fun observeTodayMeals(today: Instant): Flow<List<MealPlanRecipeEntity>>

    @Query("$MEAL_PLAN_QUERY WHERE mp.planned_for IS NULL")
    fun observeUnscheduledMeals(): Flow<List<MealPlanRecipeEntity>>

    @Query("$MEAL_PLAN_QUERY WHERE mp.planned_for BETWEEN date(:startDate) AND date(:endDate) ORDER BY mp.planned_for")
    fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlanRecipeEntity>>

    @Query("SELECT * FROM meal_planner WHERE id =:recipeId")
    suspend fun getRecipe(recipeId: Long): MealPlanEntity?

    @Query("SELECT id FROM meal_planner WHERE id = :id")
    fun hasRecipe(id: Long): Flow<Long?>

    @Query("SELECT id FROM meal_planner")
    suspend fun getAllIds(): List<Long>
}
