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
import java.time.Instant

internal abstract class BaseRecipeRepository(
    private val api: FeastAPIService,
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): RecipesRepository {

    protected abstract val recipesMapper: RecipesMapper

    protected suspend fun saveRemoteRecipes(
        dtos: List<RecipeDTO>,
        page: Int,
        request: SyncType
    ) {
        val recipeEntities = recipesMapper.jsonToEntities(dtos)
        db.blockTransaction {
            if (page == 1) {
                val currentSynced = LastSyncEntity(
                    id = 0,
                    entityType = request.name,
                    lastSyncedAt = Instant.now()
                )
                db.lastSyncDao().insertEntity(currentSynced)
                deleteRecipes(request)
            } else {
                deletePage(page, request)
            }
            insertInLocalDB(dtos, page, request)
            chunkUpdate(recipeEntities)
        }
    }

    private suspend fun insertInLocalDB(
        dtos: List<RecipeDTO>,
        page: Int,
        request: SyncType
    ) {
        when(request) {
            SyncType.POPULAR_RECIPES -> {
                val popularRecipeEntities = recipesMapper.jsonToPopularEntities(dtos, page)
                db.popularRecipeDAO().insert(popularRecipeEntities)
            }
            SyncType.TOP_RATED_RECIPES -> {
                val topRecipeEntities = recipesMapper.jsonToTopEntities(dtos, page)
                db.topRecipesDAO().insert(topRecipeEntities)
            }
            SyncType.HEALTHY_RECIPES -> {
                val healthyRecipeEntities = recipesMapper.jsonToHealthyEntities(dtos, page)
                db.healthyRecipeDAO().insert(healthyRecipeEntities)
            }
            else -> throw IllegalArgumentException("$request cannot be handled")

        }
    }

    private suspend fun deletePage(page: Int, request: SyncType) {
        when(request) {
            SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().deletePage(page)
            SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().deletePage(page)
            SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().deletePage(page)
            else -> throw IllegalArgumentException("$request cannot be handled")
        }
    }

    private suspend fun deleteRecipes(request: SyncType) {
        when(request) {
            SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().deleteRecipes()
            SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().deleteRecipes()
            SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().deleteRecipes()
            else -> throw IllegalArgumentException("$request cannot be handled")
        }
    }

    private suspend fun chunkUpdate(entities: List<RecipeEntity>) {
        for (chunk in entities.chunked(LIMIT_ITEMS)) {
            db.recipeDAO().insertRecipes(chunk)
        }
    }

}