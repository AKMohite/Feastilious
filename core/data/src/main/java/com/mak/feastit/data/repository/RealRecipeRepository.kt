package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipeDetailMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity
import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.Shopping
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        val local = db.recipeDAO().getRecipe(id).firstOrNull()
        if (local != null && !needRefresh(forceRefresh, id, SyncType.RECIPE_DETAILS)) return@withContext
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

    override suspend fun refreshAnalyzedInstruction(
        id: Long,
        forceRefresh: Boolean
    ) = withContext(dispatcher.io) {
        val local = db.recipeStepDAO().getCountForRecipe(id)
        if (local > 0 && !needRefresh(forceRefresh, id, SyncType.RECIPE_DETAIL_ANALYZED_INSTRUCTIONS)) return@withContext
        val query = mapOf(
            "stepBreakdown" to true.toString()
        )
        val instructionsDTO = api.getAnalyzedInstructions(recipeId = id, query = query)
//        val ingredientEntities = mapper.jsonToAnalysedIngredientsEntity(id, instructionsDTO)
        val stepEntities = mapper.jsonToStepEntities(id, instructionsDTO)
        saveRemoteInstructions(stepEntities, id)

    }

    override suspend fun refreshSimilarRecipes(
        id: Long,
        forceRefresh: Boolean
    ) = withContext(dispatcher.io) {
        val local = db.similarRecipeDao().getCountForRecipe(id)
        if (local > 0 && !needRefresh(forceRefresh, id, SyncType.SIMILAR_RECIPES)) return@withContext
        val query = mapOf( "number" to "10" )
        val dto = api.getSimilarRecipes(recipeId = id, query = query)
        val ids = dto.map { it.id }
        val existingRecipes = db.recipeDAO().getRecipes(ids).associateBy { recipe -> recipe.id }
        val entities = mapper.jsonToRecipeEntities(dto, existingRecipes)
        val similarEntities = mapper.jsonToSimilarEntities(dto, id)
        saveRemoteSimilarRecipes(entities, similarEntities)
    }

    override suspend fun toggleFavorite(recipeId: Long) = withContext(dispatcher.io) {
        val local = db.recipeDAO().getRecipe(recipeId).firstOrNull() ?: return@withContext
        db.recipeDAO().update(local.copy(isAddedToCollection = !local.isAddedToCollection))
    }

    override suspend fun toggleShoppingIngredients(recipeId: Long) = withContext(dispatcher.io) {
        val shopping = db.shoppingDAO().getCart(recipeId)
        if (shopping.isEmpty()) {
//            add to shopping
            val ingredients = db.ingredientDAO().getIngredientsFor(recipeId).firstOrNull() ?: return@withContext
            val shoppingCart = mapper.ingredientsToShoppingCarts(ingredients)
            db.shoppingDAO().insert(shoppingCart)
        } else {
            db.shoppingDAO().deleteCart(recipeId)
        }
    }


    override suspend fun toggleShoppingIngredient(ingredientId: String) = withContext(dispatcher.io) {
        val shopping = db.shoppingDAO().getIngredient(ingredientId)
        if (shopping == null) {
//            add to shopping
            val ingredient = db.ingredientDAO().getIngredient(ingredientId) ?: return@withContext
            val shoppingCart = mapper.ingredientToShoppingCart(ingredient)
            db.shoppingDAO().insert(shoppingCart)
        } else {
            db.shoppingDAO().delete(shopping)
        }
    }

    override fun observerRecipe(id: Long): Flow<RecipeDetail> {
        return db.recipeDAO().getRecipe(id)
            .filterNotNull()
            .flowOn(dispatcher.io)
            .map { entity ->
                mapper.entityToModel(entity)
            }.flowOn(dispatcher.computation)
    }

    override fun observerSimilarRecipes(id: Long): Flow<List<Recipe>> {
        return db.similarRecipeDao().getRecipes(id)
            .flowOn(dispatcher.io)
            .map { entity ->
                mapper.entityToRecipe(entity)
            }.flowOn(dispatcher.computation)
    }

    override fun observeIngredients(id: Long): Flow<List<Ingredient>> {
        return db.ingredientDAO().getIngredientsFor(id)
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities ->
                mapper.entitiesToIngredients(entities)
            }.flowOn(dispatcher.io)
    }

    override fun observeShoppingCartForRecipe(recipeId: Long): Flow<List<Shopping>> {
        return db.shoppingDAO().observeCartForRecipe(recipeId)
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities ->
                mapper.entityToShopping(entities)
            }.flowOn(dispatcher.computation)
    }

    override fun observeInstructions(id: Long): Flow<List<Instruction>> {
        return db.recipeStepDAO().getStepsFor(id)
            .flowOn(dispatcher.io)
            .map { entities ->
                mapper.entitiesToSteps(entities)
            }.flowOn(dispatcher.computation)
    }

    private suspend fun saveRemoteSimilarRecipes(entities: List<RecipeEntity>, similarEntities: List<SimilarRecipeEntity>) {
        if (similarEntities.isEmpty()) return
        db.handleTransaction {
            val recipeId = similarEntities.first().parentRecipeId
            db.recipeDAO().upsert(entities)
            db.similarRecipeDao().deleteRecipe(recipeId)
            db.similarRecipeDao().insert(similarEntities)
            val currentSynced = LastSyncEntity(
                id = 0L,
                entityType = SyncType.SIMILAR_RECIPES.name,
                entityId = recipeId,
                lastSyncedAt = Instant.now()
            )
            db.lastSyncDao().insert(currentSynced)
        }
    }

    private suspend fun saveRemoteInstructions(stepEntities: List<RecipeStepEntity>, recipeId: Long) {
        db.handleTransaction {
            db.recipeStepDAO().deleteRecipe(recipeId = recipeId)
            db.recipeStepDAO().insert(stepEntities)
            val currentSynced = LastSyncEntity(
                id = 0L,
                entityType = SyncType.RECIPE_DETAIL_ANALYZED_INSTRUCTIONS.name,
                entityId = stepEntities.first().recipeId,
                lastSyncedAt = Instant.now()
            )
            db.lastSyncDao().insert(currentSynced)
        }
    }

    private suspend fun saveRemoteRecipe(
        entity: RecipeEntity,
        ingredientEntities: List<IngredientEntity>
    ) {
        db.handleTransaction {
            db.recipeDAO().upsert(entity)
            db.ingredientDAO().deleteRecipe(entity.id)
//            TODO delete nutrition
            db.ingredientDAO().upsert(ingredientEntities)
//            TODO save nutrition
            val currentSynced = LastSyncEntity(
                id = 0,
                entityType = SyncType.RECIPE_DETAILS.name,
                entityId = entity.id,
                lastSyncedAt = Instant.now()
            )
            db.lastSyncDao().insert(currentSynced)
        }
    }

    private suspend fun needRefresh(
        forceRefresh: Boolean,
        id: Long,
        syncType: SyncType,
        duration: Duration = Duration.of(60, ChronoUnit.DAYS)
    ): Boolean {
        if (!forceRefresh) {
            val lastSynced = db.lastSyncDao().getLastSync(syncType.name, id)
//        TODO validity duration can be less but for now kept 6hours
            if (lastSynced != null && isRequestValid(lastSynced.lastSyncedAt, duration)) {
                return false
            }
        }
        return true
    }

    private fun isRequestValid(lastSyncedAt: Instant, duration: Duration): Boolean {
        return lastSyncedAt > (Instant.now() - duration)
    }
}