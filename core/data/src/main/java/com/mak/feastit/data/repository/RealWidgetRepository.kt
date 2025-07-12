// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.repository.WidgetRepository
import com.mak.feastit.domain.util.DispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class RealWidgetRepository @Inject constructor(
  private val db: FeastDB,
  private val dispatcher: DispatcherProvider,
) : WidgetRepository {
  private val recipesMapper = RecipesMapper()

  override suspend fun getFavoriteRecipes(): List<Recipe> = withContext(dispatcher.io) {
    val favorites = db.recipeDAO().getFavoriteRecipesForWidget().firstOrNull() ?: return@withContext emptyList()
    recipesMapper.entitiesToModels(favorites)
  }
}
