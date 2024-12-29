package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import kotlinx.coroutines.flow.filterNotNull

internal class RealRecipeRepository(
    private val api: FeastAPIService,
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): RecipeRepository {

    override suspend fun refreshRecipe(id: Long, forceRefresh: Boolean) {

    }

    override fun getRecipe(id: Long) {
        db.recipeDAO().getRecipe(id)
            .filterNotNull()
    }
}