package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.database.entity.custom.MealPlanRecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface MealPlannerDAO: BaseDAO<MealPlanEntity> {

    @Query("SELECT mp.id, mp.planned_for, mp.is_made, r.name, r.img FROM meal_planner mp INNER JOIN recipes r ON mp.id = r.id WHERE mp.planned_for = date(:today)")
    fun observeTodayMeals(today: Instant): Flow<List<MealPlanRecipeEntity>>

    @Query("SELECT mp.id, mp.planned_for, mp.is_made, r.name, r.img FROM meal_planner mp INNER JOIN recipes r ON mp.id = r.id WHERE mp.planned_for IS NULL")
    fun observeUnscheduledMeals(): Flow<List<MealPlanRecipeEntity>>

    @Query("SELECT mp.id, mp.planned_for, mp.is_made, r.name, r.img FROM meal_planner mp INNER JOIN recipes r ON mp.id = r.id WHERE mp.planned_for BETWEEN date(:startDate) AND date(:endDate)")
    fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlanRecipeEntity>>

    @Query("SELECT * FROM meal_planner WHERE id =:recipeId")
    suspend fun getRecipe(recipeId: Long): MealPlanEntity?

    @Query("SELECT id FROM meal_planner WHERE id = :id")
    fun hasRecipe(id: Long): Flow<Long?>

    @Query("SELECT id FROM meal_planner")
    suspend fun getAllIds(): List<Long>
}
