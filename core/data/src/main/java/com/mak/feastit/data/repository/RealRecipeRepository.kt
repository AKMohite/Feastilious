package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipeDetailMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

internal class RealRecipeRepository @Inject constructor(
    private val api: FeastAPIService,
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider,
    private val mapper: RecipeDetailMapper
): RecipeRepository {

    override suspend fun refreshRecipe(
        id: Long,
        forceRefresh: Boolean
    ) = withContext(dispatcher.io) {
        if (!forceRefresh) {
            val lastSynced = db.lastSyncDao().getLastSync(SyncType.RECIPE_DETAILS.name, id)
//        TODO validity duration can be less but for now kept 6hours
            val duration = Duration.of(60, ChronoUnit.DAYS)
            if (lastSynced != null && isRequestValid(lastSynced.lastSyncedAt, duration)) {
                return@withContext
            }
        }
        val query = mapOf(
            "includeNutrition" to true.toString(),
            "addWinePairing" to false.toString(),
            "addTasteData" to false.toString()
        )
        val recipeDTO = api.getRecipe(recipeId = id, recipeQuery = query)
        val nutrients = recipeDTO.nutrition?.nutrients
        val ingredients = recipeDTO.extendedIngredients
        val calorieBreakdown = recipeDTO.nutrition?.caloricBreakdown

        val recipeEntity = mapper.jsonToEntity(recipeDTO)
        val ingredientEntities = mapper.jsonToIngredientsEntity(recipeDTO.id, ingredients)
//        TODO add nutrition table
//        val nutritionEntities = mapper.jsonToNutritionEntity(recipeDTO.id, nutrients, calorieBreakdown)
        saveRemoteRecipe(recipeEntity, ingredientEntities)
    }

    override fun getRecipe(id: Long) {
        db.recipeDAO().getRecipe(id)
            .filterNotNull()
    }

    private suspend fun saveRemoteRecipe(
        entity: RecipeEntity,
        ingredientEntities: List<IngredientEntity>
    ) {
        db.handleTransaction {
            db.recipeDAO().insertRecipe(entity)
            db.recipeDAO().insertIngredients(ingredientEntities)
//            TODO save nutrition
            val currentSynced = LastSyncEntity(
                id = 0,
                entityType = SyncType.RECIPE_DETAILS.name,
                entityId = entity.id,
                lastSyncedAt = Instant.now()
            )
            db.lastSyncDao().insertEntity(currentSynced)
        }
    }

    private fun isRequestValid(lastSyncedAt: Instant, duration: Duration): Boolean {
        return lastSyncedAt > (Instant.now() - duration)
    }
}