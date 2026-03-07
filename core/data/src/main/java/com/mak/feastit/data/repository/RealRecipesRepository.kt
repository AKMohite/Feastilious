// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.data.paging.DiscoverRemoteMediator
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.custom.PaginatedRecipeEntity
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.RecipeDTO
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import androidx.core.net.toUri
import com.mak.feastit.domain.model.ImageType
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.model.RecipeImageSize
import dagger.hilt.android.qualifiers.ApplicationContext

const val LIMIT_ITEMS = 20

internal class RealRecipesRepository
@Inject
constructor(
  @ApplicationContext private val context: Context,
  private val api: FeastAPIService,
  private val db: FeastDB,
  private val dispatcher: DispatcherProvider,
) : BaseRecipeRepository(
  db = db,
) {
  override val recipesMapper = RecipesMapper()

  override suspend fun refreshRecipes(
    request: SyncType,
    page: Int,
    forceRefresh: Boolean,
  ) = withContext(dispatcher.io) {
    if (page > 5) return@withContext // TODO Use pro just have limited API calls condition can be removed
    if (!forceRefresh) { // TODO on force refresh delete all local data??? need to handle favorites, meal planner and shopping kart
      val hasLocalData = isLocallyAvailable(page, request)
      val lastSynced = db.lastSyncDao().getLastSync(request.name)
//        TODO validity duration can be less but for now kept 6hours
      if (hasLocalData && lastSynced != null && isRequestValid(lastSynced.lastSyncedAt, 60.days)) {
        return@withContext
      }
    }
    Timber.d("Refresh recipes for $request with page: $page")
    val dtos = fetchRecipes(request, page) ?: return@withContext
    if (dtos.isEmpty()) return@withContext
    saveRemoteRecipes(dtos, page, request)
  }

  override fun getRecipes(
    request: SyncType,
    page: Int,
  ): Flow<List<Recipe>> {
    Timber.d("Observe recipes for $request and page: $page")
    return when (request) {
      SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().getRecipes(page)
      SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().getRecipes(page)
      SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().getRecipes(page)
      SyncType.QUICK_RECIPES -> db.quickRecipeDAO().getRecipes(page)
      SyncType.POCKET_FRIENDLY_RECIPES -> db.pocketFriendlyRecipeDAO().getRecipes(page)
      else -> throw IllegalArgumentException("$request must not be requested")
    }.distinctUntilChanged()
      .flowOn(dispatcher.io)
      .map(recipesMapper::entitiesToModels)
      .flowOn(dispatcher.computation)
  }

  @OptIn(ExperimentalPagingApi::class)
  override fun observePaginatedRecipes(
    request: SyncType,
    pagingConfig: PagingConfig,
  ): Flow<PagingData<Recipe>> {
    return Pager(
      config = pagingConfig,
      remoteMediator =
      DiscoverRemoteMediator(
        fetch = { page ->
          refreshRecipes(request, page, false)
        },
        isCacheValid = {
          withContext(dispatcher.io) {
//                        val hasLocalData = isLocallyAvailable(1, request)
//                        val lastSynced = db.lastSyncDao().getLastSync(request.name)
//                        if (hasLocalData && lastSynced != null && isRequestValid(lastSynced.lastSyncedAt, 60.days)) {
//                            return@withContext
//                        }
            val lastSynced =
              db.lastSyncDao().getLastSync(request.name) ?: return@withContext false
            Timber.d("Last synced at: ${lastSynced.lastSyncedAt}")
            isRequestValid(lastSynced.lastSyncedAt, 60.days)
          }
        },
      ),
      pagingSourceFactory = { getPagedRecipes(request) },
    ).flow
      .flowOn(dispatcher.io)
      .map { pagingData ->
        pagingData.map(recipesMapper::paginatedEntityToModel)
      }.flowOn(dispatcher.computation)
  }

  override fun observeQueryPaginatedRecipes(
    subType: String,
    pagingConfig: PagingConfig,
  ): Flow<PagingData<Recipe>> {
    TODO("Not yet implemented")
  }

  override suspend fun searchRecipe(query: String): List<Recipe> = withContext(dispatcher.io) {
    val entities = db.recipeDAO().searchRecipes(query)
    Timber.d("Found ${entities.size} recipes")
    recipesMapper.entitiesToModels(entities)
  }

  override suspend fun searchRecipeByImage(imagePath: String): List<Recipe> = withContext(dispatcher.io) {
    val uri = imagePath.toUri()
    val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext emptyList()
    val bytes = inputStream.readBytes()
    inputStream.close()
    val a = context.contentResolver.getType(uri)
    if (bytes.isEmpty()) return@withContext emptyList()
    val recipeDTOs = api.searchRecipesByBytes(bytes, a).recipes.orEmpty()
    recipeDTOs.map { dto ->
      Recipe(
        dto.id!!,
        dto.title.orEmpty(),
        RecipeImage(dto.id!!, dto.imageType.orEmpty(), RecipeImageSize.MEDIUM, ImageType.CELL),
        page = 1
      )
    }
  }

  private fun getPagedRecipes(request: SyncType): PagingSource<Int, PaginatedRecipeEntity> {
//        TODO maybe request items using offset and limit instead of paging data?
    val recipes =
      when (request) {
        SyncType.POPULAR_RECIPES -> db.popularRecipeDAO().pagedRecipes()
        SyncType.TOP_RATED_RECIPES -> db.topRecipesDAO().pagedRecipes()
        SyncType.HEALTHY_RECIPES -> db.healthyRecipeDAO().pagedRecipes()
        SyncType.QUICK_RECIPES -> db.quickRecipeDAO().pagedRecipes()
        SyncType.POCKET_FRIENDLY_RECIPES -> db.pocketFriendlyRecipeDAO().pagedRecipes()
        else -> throw IllegalArgumentException("Paged entries are not available for $request")
      }
    return recipes
  }

  override fun observeFavoriteRecipes(): Flow<List<Recipe>> = db
    .recipeDAO()
    .observeFavoriteRecipes()
    .distinctUntilChanged()
    .flowOn(dispatcher.io)
    .map { entities ->
      recipesMapper.entitiesToModels(entities)
    }.flowOn(dispatcher.computation)

  override fun observeSearchSuggestions(query: String): Flow<List<Recipe>> {
    Timber.d("Search for $query")
    return db
      .recipeDAO()
      .searchFavRecipes(query)
      .distinctUntilChanged()
      .flowOn(dispatcher.io)
      .map { entities ->
        recipesMapper.entitiesToModels(entities)
      }.flowOn(dispatcher.computation)
  }

  private suspend fun fetchRecipes(
    request: SyncType,
    page: Int,
  ): List<RecipeDTO>? {
    val searchParams =
      mutableMapOf(
        "number" to LIMIT_ITEMS.toString(),
        "offset" to getOffset(page),
        "sortDirection" to "desc",
      )
    when (request) {
      SyncType.POPULAR_RECIPES -> searchParams["sort"] = "popularity"
      SyncType.TOP_RATED_RECIPES -> searchParams["sort"] = "meta-score"
      SyncType.HEALTHY_RECIPES -> searchParams["sort"] = "healthiness"
      SyncType.QUICK_RECIPES -> searchParams["sort"] = "time" // TODO check sort direction
      SyncType.POCKET_FRIENDLY_RECIPES -> searchParams["sort"] =
        "price" // TODO check sort direction
      else -> throw IllegalArgumentException("$request must not be requested")
    }
    return api.searchRecipes(searchParams).results
  }
}
