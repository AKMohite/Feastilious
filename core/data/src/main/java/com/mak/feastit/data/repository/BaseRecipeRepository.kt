package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.remote.dto.RecipeDTO
import kotlinx.datetime.Instant
import timber.log.Timber
import kotlin.time.Duration

internal abstract class BaseRecipeRepository(
    private val db: FeastDB
): RecipesRepository {

    protected abstract val recipesMapper: RecipesMapper

    protected suspend fun saveRemoteRecipes(
        dtos: List<RecipeDTO>,
        page: Int,
        request: SyncType
    ) {
        Timber.d("Save recipes for $request")
        val recipeEntities = recipesMapper.jsonToEntities(dtos)
        db.handleTransaction {
            if (page == 1) {
                val currentSynced = LastSyncEntity(
                    id = 0,
                    entityType = request.name,
                    lastSyncedAt = defaultNow()
                )
                db.lastSyncDao().insert(currentSynced)
                deleteRecipes(request)
            } else {
                deletePage(page, request)
            }
            chunkUpdate(recipeEntities)
            insertInLocalDB(dtos, page, request)
        }
    }

    protected fun isRequestValid(lastSyncedAt: Instant, duration: Duration): Boolean {
        return lastSyncedAt > (defaultNow() - duration)
    }

    protected fun getOffset(page: Int): String {
        if (page < 1) throw IllegalStateException("page must be greater than 0")
        return ((page - 1) * LIMIT_ITEMS).toString()
    }

    private suspend fun insertInLocalDB(
        dtos: List<RecipeDTO>,
        page: Int,
        request: SyncType
    ) {
        Timber.d("Insert recipes in DB for $request")
        when(request) {
            SyncType.POPULAR_RECIPES -> {
                val popularRecipeEntities = recipesMapper.jsonToPopularEntities(dtos, page)
                for (chunk in popularRecipeEntities.chunked(LIMIT_ITEMS)) {
                    db.popularRecipeDAO().insert(chunk)
                }
            }
            SyncType.TOP_RATED_RECIPES -> {
                val topRecipeEntities = recipesMapper.jsonToTopEntities(dtos, page)
                for (chunk in topRecipeEntities.chunked(LIMIT_ITEMS)) {
                    db.topRecipesDAO().insert(chunk)
                }
            }
            SyncType.HEALTHY_RECIPES -> {
                val healthyRecipeEntities = recipesMapper.jsonToHealthyEntities(dtos, page)
                for (chunk in healthyRecipeEntities.chunked(LIMIT_ITEMS)) {
                    db.healthyRecipeDAO().insert(chunk)
                }
            }
            SyncType.QUICK_RECIPES -> {
                val quickRecipeEntities = recipesMapper.jsonToQuickEntities(dtos, page)
                for (chunk in quickRecipeEntities.chunked(LIMIT_ITEMS)) {
                    db.quickRecipeDAO().insert(chunk)
                }
            }
            SyncType.POCKET_FRIENDLY_RECIPES -> {
                val pocketFriendlyRecipeEntities = recipesMapper.jsonToPocketFriendlyEntities(dtos, page)
                for (chunk in pocketFriendlyRecipeEntities.chunked(LIMIT_ITEMS)) {
                    db.pocketFriendlyRecipeDAO().insert(chunk)
                }
            }
            else -> throw IllegalArgumentException("$request cannot be persisted")

        }
    }

    private suspend fun deletePage(page: Int, request: SyncType) {
        Timber.d("Delete recipes for page $page and for request $request")
        when(request) {
            SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().deletePage(page)
            SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().deletePage(page)
            SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().deletePage(page)
            SyncType.QUICK_RECIPES -> db.quickRecipeDAO().deletePage(page)
            SyncType.POCKET_FRIENDLY_RECIPES -> db.pocketFriendlyRecipeDAO().deletePage(page)
            else -> throw IllegalArgumentException("$request cannot be manipulated")
        }
    }

    private suspend fun deleteRecipes(request: SyncType) {
        Timber.d("Delete recipes for $request")
        when(request) {
            SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().deleteRecipes()
            SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().deleteRecipes()
            SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().deleteRecipes()
            SyncType.QUICK_RECIPES -> db.quickRecipeDAO().deleteRecipes()
            SyncType.POCKET_FRIENDLY_RECIPES -> db.pocketFriendlyRecipeDAO().deleteRecipes()
            else -> throw IllegalArgumentException("$request cannot be manipulated")
        }
    }

    private suspend fun chunkUpdate(entities: List<RecipeEntity>) {
        Timber.d("Chunk update recipes")
        for (chunk in entities.chunked(LIMIT_ITEMS)) {
            db.recipeDAO().upsert(chunk)
        }
    }

    suspend fun isLocallyAvailable(page: Int, request: SyncType): Boolean {
        return when(request) {
            SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().getCount(page) > 0
            SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().getCount(page) > 0
            SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().getCount(page) > 0
            SyncType.QUICK_RECIPES -> db.quickRecipeDAO().getCount(page) > 0
            SyncType.POCKET_FRIENDLY_RECIPES -> db.pocketFriendlyRecipeDAO().getCount(page) > 0
            else -> throw IllegalArgumentException("$request should not be requested")
        }
    }

}