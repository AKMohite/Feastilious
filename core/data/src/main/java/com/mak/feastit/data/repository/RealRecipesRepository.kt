package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.PaginatedParams
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.RecipeDTO

const val LIMIT_ITEMS = 20

internal class RealRecipesRepository(
    private val api: FeastAPIService,
    private val db: FeastDB
): RecipesRepository {

    private val recipesMapper = RecipesMapper()


    override suspend fun refreshRecipes(request: SyncType, page: Int) {
        val dtos = fetchRecipes(request, page)
        val recipeEntities = recipesMapper.jsonToEntities(dtos)
        val popularRecipeEntities = recipesMapper.jsonToEntities(dtos)
        db.blockTransaction {
            if (page == 1) {
//                db.lastSyncDao().updateLastSync(request)
//                db.popularRecipeDAO().deletePopularRecipes()
//                db.popularRecipeDAO().insert(popularRecipeEntities)
            } else {
//                db.popularRecipeDAO().deletePage(page)
//                db.popularRecipeDAO().updatePage(popularRecipeEntities)
            }
            chunkUpdate(recipeEntities)
        }
    }

    private suspend fun fetchRecipes(request: SyncType, page: Int): List<RecipeDTO> {
        val searchParams = mutableMapOf(
            "number" to LIMIT_ITEMS.toString(),
            "offset" to getOffset(page),
            "sortDirection" to "desc"
        )
        when(request) {
            SyncType.POPULAR_RECIPES -> searchParams["sort"] = "popularity"
            SyncType.TOP_RATED_RECIPES -> searchParams["sort"] = "meta-score"
            SyncType.HEALTHY_RECIPES -> searchParams["sort"] = "healthiness"
            SyncType.QUICK_RECIPES -> searchParams["sort"] = "time" // TODO check sort direction
            SyncType.POCKET_FRIENDLY_RECIPES -> searchParams["sort"] = "price" // TODO check sort direction
            else -> throw IllegalArgumentException("$request must not be requested")
        }
        return api.searchRecipes(searchParams).results ?: emptyList()
    }

    private fun getOffset(page: Int): String {
        if (page < 1) throw IllegalStateException("page must be greater than 0")
        return ((page - 1) * LIMIT_ITEMS).toString()
    }
    private suspend fun chunkUpdate(entities: List<RecipeEntity>) {
        for (chunk in entities.chunked(20)) {
            db.recipeDAO().insertRecipes(chunk)
        }
    }
}