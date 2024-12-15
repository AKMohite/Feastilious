package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.RecipeDTO
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

const val LIMIT_ITEMS = 20

internal class RealRecipesRepository @Inject constructor(
    private val api: FeastAPIService,
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): RecipesRepository {

    private val recipesMapper = RecipesMapper()


    override suspend fun refreshRecipes(request: SyncType, page: Int, forceRefresh: Boolean) = withContext(dispatcher.io) {
        if (!forceRefresh) {
            val lastSynced = db.lastSyncDao().getLastSync(request.name)
            if (lastSynced != null && isRequestValid(lastSynced.lastSyncedAt)) {
                return@withContext
            }
        }
        val dtos = fetchRecipes(request, page) ?: return@withContext
        if (dtos.isEmpty()) return@withContext
        val recipeEntities = recipesMapper.jsonToEntities(dtos)
        val popularRecipeEntities = recipesMapper.jsonToPopularEntities(dtos, page)
        db.blockTransaction {
            if (page == 1) {
                val currentSynced = LastSyncEntity(
                    id = 0,
                    entityType = request.name,
                    lastSyncedAt = Instant.now()
                )
                db.lastSyncDao().insertEntity(currentSynced)
                db.popularRecipeDAO().deletePopularRecipes()
            } else {
                db.popularRecipeDAO().deletePage(page)
            }
            db.popularRecipeDAO().insert(popularRecipeEntities)
            chunkUpdate(recipeEntities)
        }
    }

    private fun isRequestValid(lastSyncedAt: Instant): Boolean {
        return lastSyncedAt > (Instant.now() - Duration.of(3, ChronoUnit.HOURS))
    }

    private suspend fun fetchRecipes(request: SyncType, page: Int): List<RecipeDTO>? {
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
        return api.searchRecipes(searchParams).results
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