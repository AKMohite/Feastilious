package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface MealPlannerDAO: BaseDAO<MealPlanEntity> {

    @Query("SELECT * FROM meal_planner WHERE planned_for = date(:today)")
    fun observeTodayMeals(today: Instant): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM meal_planner WHERE planned_for IS NULL")
    fun observeUnscheduledMeals(): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM meal_planner WHERE planned_for BETWEEN date(:startDate) AND date(:endDate)")
    fun observeWeekMeals(startDate: Instant, endDate: Instant): Flow<List<MealPlanEntity>>
}
