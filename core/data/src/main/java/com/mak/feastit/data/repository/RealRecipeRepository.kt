// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipeDetailMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.NutrientEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity
import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.remote.FeastAPIService
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class RealRecipeRepository @Inject constructor(
  private val api: FeastAPIService,
  private val db: FeastDB,
  private val dispatcher: DispatcherProvider,
  private val mapper: RecipeDetailMapper,
) : RecipeRepository {
  override suspend fun refreshRecipe(
    id: Long,
    forceRefresh: Boolean,
  ) = withContext(dispatcher.io) {
    val local = db.recipeDAO().getRecipe(id).firstOrNull()
    if (local != null && !needRefresh(forceRefresh, id, SyncType.RECIPE_DETAILS)) return@withContext
    Timber.d("Refreshing recipe: $id")
    val query =
      mapOf(
        "includeNutrition" to true.toString(),
        "addWinePairing" to false.toString(),
        "addTasteData" to false.toString(),
      )
    val recipeDTO = api.getRecipe(recipeId = id, recipeQuery = query)
    val nutrients = recipeDTO.nutrition?.nutrients ?: emptyList()
    val ingredients = recipeDTO.extendedIngredients
    val calorieBreakdown = recipeDTO.nutrition?.caloricBreakdown ?: emptyMap()

    val recipeEntity = mapper.jsonToEntity(recipeDTO, calorieBreakdown)
    val ingredientEntities = mapper.jsonToIngredientsEntity(recipeDTO.id, ingredients)
    val nutritionEntities = mapper.jsonToNutritionEntity(recipeDTO.id, nutrients)
    saveRemoteRecipe(recipeEntity, ingredientEntities, nutritionEntities)
  }

  override suspend fun refreshAnalyzedInstruction(
    id: Long,
    forceRefresh: Boolean,
  ) = withContext(dispatcher.io) {
    val local = db.recipeStepDAO().getCountForRecipe(id)
    if (local > 0 &&
      !needRefresh(
        forceRefresh,
        id,
        SyncType.RECIPE_ANALYZED_INSTRUCTIONS,
      )
    ) {
      return@withContext
    }
    Timber.d("Refresh recipe instructions: $id")
    val query =
      mapOf(
        "stepBreakdown" to true.toString(),
      )
    val instructionsDTO = api.getAnalyzedInstructions(recipeId = id, query = query)
//        val ingredientEntities = mapper.jsonToAnalysedIngredientsEntity(id, instructionsDTO)
    val stepEntities = mapper.jsonToStepEntities(id, instructionsDTO)
    saveRemoteInstructions(stepEntities, id)
  }

  override suspend fun refreshSimilarRecipes(
    id: Long,
    forceRefresh: Boolean,
  ) = withContext(dispatcher.io) {
    val local = db.similarRecipeDao().getCountForRecipe(id)
    if (local > 0 &&
      !needRefresh(
        forceRefresh,
        id,
        SyncType.RECIPE_WITH_SIMILAR_RECIPES,
      )
    ) {
      return@withContext
    }
    Timber.d("Refresh similar recipes: $id")
    val query = mapOf("number" to "10")
    val dto = api.getSimilarRecipes(recipeId = id, query = query)
    val ids = dto.map { it.id }
    val existingRecipes = db.recipeDAO().getRecipes(ids).associateBy { recipe -> recipe.id }
    val entities = mapper.jsonToRecipeEntities(dto, existingRecipes)
    val similarEntities = mapper.jsonToSimilarEntities(dto, id)
    saveRemoteSimilarRecipes(entities, similarEntities)
  }

  override suspend fun toggleFavorite(recipeId: Long) = withContext(dispatcher.io) {
    val local = db.recipeDAO().getRecipe(recipeId).firstOrNull() ?: return@withContext
    val isAddedToCollection = !local.isAddedToCollection
    Timber.d("Recipe is favorite: $isAddedToCollection")
    db.recipeDAO().update(local.copy(isAddedToCollection = isAddedToCollection))
  }

  override fun observerRecipe(id: Long): Flow<RecipeDetail> = db
    .recipeDAO()
    .getRecipe(id)
    .filterNotNull()
    .flowOn(dispatcher.io)
    .map { entity ->
      mapper.entityToModel(entity)
    }.flowOn(dispatcher.computation)

  override fun observerSimilarRecipes(id: Long): Flow<List<Recipe>> = db
    .similarRecipeDao()
    .getRecipes(id)
    .flowOn(dispatcher.io)
    .map { entity ->
      mapper.entityToRecipe(entity)
    }.flowOn(dispatcher.computation)

  override fun observeIngredients(id: Long): Flow<List<Ingredient>> = db
    .ingredientDAO()
    .getIngredientsFor(id)
    .distinctUntilChanged()
    .flowOn(dispatcher.io)
    .map { entities ->
      mapper.entitiesToIngredients(entities)
    }.flowOn(dispatcher.io)

  override fun observeInstructions(id: Long): Flow<List<Instruction>> = db
    .recipeStepDAO()
    .getStepsFor(id)
    .flowOn(dispatcher.io)
    .map { entities ->
      mapper.entitiesToSteps(entities)
    }.flowOn(dispatcher.computation)

  private suspend fun saveRemoteSimilarRecipes(
    entities: List<RecipeEntity>,
    similarEntities: List<SimilarRecipeEntity>,
  ) {
    if (similarEntities.isEmpty()) return
    Timber.d("Save similar recipes for recipe: ${entities.firstOrNull()?.id}")
    db.handleTransaction {
      val recipeId = similarEntities.first().parentRecipeId
      db.recipeDAO().upsert(entities)
      db.similarRecipeDao().deleteRecipe(recipeId)
      db.similarRecipeDao().insert(similarEntities)
      val currentSynced =
        LastSyncEntity(
          id = 0L,
          entityType = SyncType.RECIPE_WITH_SIMILAR_RECIPES.name,
          entityId = recipeId,
          lastSyncedAt = defaultNow(),
        )
      db.lastSyncDao().insert(currentSynced)
    }
  }

  private suspend fun saveRemoteInstructions(
    stepEntities: List<RecipeStepEntity>,
    recipeId: Long,
  ) {
    Timber.d("Save recipe instructions for: $recipeId")
    db.handleTransaction {
      db.recipeStepDAO().deleteRecipe(recipeId = recipeId)
      db.recipeStepDAO().insert(stepEntities)
      val currentSynced =
        LastSyncEntity(
          id = 0L,
          entityType = SyncType.RECIPE_ANALYZED_INSTRUCTIONS.name,
          entityId = stepEntities.first().recipeId,
          lastSyncedAt = defaultNow(),
        )
      db.lastSyncDao().insert(currentSynced)
    }
  }

  private suspend fun saveRemoteRecipe(
    entity: RecipeEntity,
    ingredientEntities: List<IngredientEntity>,
    nutritionEntities: List<NutrientEntity>,
  ) {
    Timber.d("save network recipe in database for: ${entity.id}")
    db.handleTransaction {
      db.recipeDAO().upsert(entity)
//            TODO get shopping ingredients as it will also be deleted
      db.ingredientDAO().deleteRecipe(entity.id)
      db.ingredientDAO().upsert(ingredientEntities)
//            TODO maybe avoid delete nutrients to track calories
      db.nutrientDAO().deleteRecipe(entity.id)
      db.nutrientDAO().upsert(nutritionEntities)
      val currentSynced =
        LastSyncEntity(
          id = 0,
          entityType = SyncType.RECIPE_DETAILS.name,
          entityId = entity.id,
          lastSyncedAt = defaultNow(),
        )
      db.lastSyncDao().insert(currentSynced)
    }
  }

  private suspend fun needRefresh(
    forceRefresh: Boolean,
    id: Long,
    syncType: SyncType,
    duration: Duration = 60.days,
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

  private fun isRequestValid(
    lastSyncedAt: Instant,
    duration: Duration,
  ): Boolean = lastSyncedAt > (Clock.System.now() - duration)
}
