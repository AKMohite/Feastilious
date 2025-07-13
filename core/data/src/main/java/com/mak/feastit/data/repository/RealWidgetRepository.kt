// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.model.DayMealPlan
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.WeekMealPlanSection
import com.mak.feastit.domain.repository.WidgetRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.daysShift
import com.mak.feastit.domain.util.defaultLocalDate
import com.mak.feastit.domain.util.defaultLocalDateTime
import javax.inject.Inject
import kotlin.collections.iterator
import kotlin.collections.map
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant

internal class RealWidgetRepository @Inject constructor(
  private val db: FeastDB,
  private val dispatcher: DispatcherProvider,
) : WidgetRepository {
  private val recipesMapper = RecipesMapper()

  override suspend fun getFavoriteRecipes(): List<Recipe> = withContext(dispatcher.io) {
    val favorites = db.recipeDAO().getFavoriteRecipesForWidget().firstOrNull() ?: return@withContext emptyList()
    recipesMapper.entitiesToModels(favorites)
  }

  override suspend fun getWeekMealPlans(): List<WeekMealPlanSection> = withContext(dispatcher.io) {
    val (start, end) = getWeekRange()
    val mealPlanRecipes = db
      .mealPlanDAO()
      .observeWeekMeals(start, end)
      .flowOn(dispatcher.io)
      .map { entities ->
        if (entities.isEmpty()) return@map emptyMap<String, List<MealPlanRecipe>>()
        val weeklyMealPlans = mutableMapOf<String, List<MealPlanRecipe>>()
        val firstWeekDay = start.defaultLocalDate()
        val recipes = entities.mapEntitiesToModels().groupBy { it.scheduledFor?.date }
        for (i in 0 until DayOfWeek.entries.toTypedArray().count()) {
          val date = firstWeekDay.daysShift(i)
          weeklyMealPlans[date.dayOfWeek.name] = recipes[date] ?: emptyList()
        }
        weeklyMealPlans
      }.flowOn(dispatcher.computation)
      .firstOrNull() ?: return@withContext emptyList()
    val sections = mutableListOf<WeekMealPlanSection>()
    for (plan in mealPlanRecipes) {
      val noOfMeal = plan.value.count()
      val mealCount = if (noOfMeal == 0) {
        ""
      } else if (noOfMeal > 9) {
        "9+"
      } else {
        "$noOfMeal"
      }
      val dayMealPlan = DayMealPlan(day = plan.key, noOfMeal = mealCount)
      val header = WeekMealPlanSection.DayHeader(dayMealPlan)
      sections.add(header)
      plan.value.map { meal ->
        sections.add(WeekMealPlanSection.MealRecipe(meal = meal))
      }
    }
    sections.toList()
  }

  private fun getWeekRange(): Pair<Instant, Instant> {
    val instant = Clock.System.now()
    val nowDateTime = instant.defaultLocalDateTime()
    val dayOfWeek = DayOfWeek.entries.indexOf(nowDateTime.dayOfWeek)
    val startWeek = instant.minus(dayOfWeek.days)
    val lastIndex = DayOfWeek.entries.count() - 1 - dayOfWeek
    val lastWeek = instant.plus(lastIndex.days)
    return Pair(startWeek, lastWeek)
  }
}
