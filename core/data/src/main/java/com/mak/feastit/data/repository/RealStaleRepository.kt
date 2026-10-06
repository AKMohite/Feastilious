// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.repository.StaleRepository
import javax.inject.Inject
import timber.log.Timber

internal class RealStaleRepository
@Inject
constructor(
  private val db: FeastDB,
) : StaleRepository {
  override suspend fun removeStaleData() {
    Timber.d("Remove stale data from app")
    val neededIds = mutableSetOf<Long>()

    val healthyIds = db.healthyRecipeDAO().getAllIds()
    neededIds.addAll(healthyIds)

    val popularIds = db.popularRecipeDAO().getAllIds()
    neededIds.addAll(popularIds)

    val quickIds = db.quickRecipeDAO().getAllIds()
    neededIds.addAll(quickIds)

    val topIds = db.topRecipesDAO().getAllIds()
    neededIds.addAll(topIds)

    val similarIds = db.similarRecipeDao().getAllIds()
    neededIds.addAll(similarIds)

    val pocketIds = db.pocketFriendlyRecipeDAO().getAllIds()
    neededIds.addAll(pocketIds)

    val mealPlans = db.mealPlanDAO().getAllIds()
    neededIds.addAll(mealPlans)

    val shoppingCart = db.shoppingDAO().getAllIds()
    neededIds.addAll(shoppingCart)

    db.handleTransaction {
      db.lastSyncDao().deleteSinceDays(30)
      db.recipeDAO().deleteRecipesNotIn(neededIds)
    }
    Timber.d("All stale data removed from app")
  }
}
