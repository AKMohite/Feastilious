// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.WeekMealPlanSection

interface WidgetRepository {

  suspend fun getFavoriteRecipes(): List<Recipe>
  suspend fun getWeekMealPlans(): List<WeekMealPlanSection>
}
