package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.RecipeDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Inject

const val LIMIT_ITEMS = 20

internal class RealRecipesRepository @Inject constructor(
    private val api: FeastAPIService,
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): BaseRecipeRepository(
    db = db
) {

    override val recipesMapper = RecipesMapper()

    override suspend fun refreshRecipes(request: SyncType, page: Int, forceRefresh: Boolean) = withContext(dispatcher.io) {
        if (page > 5) return@withContext // TODO Use pro just have limited API calls condition can be removed
        if (!forceRefresh) {
            val hasLocalData = isLocallyAvailable(page, request)
            val lastSynced = db.lastSyncDao().getLastSync(request.name)
//        TODO validity duration can be less but for now kept 6hours
            val duration = Duration.of(60, ChronoUnit.DAYS)
//            val duration = Period.ofWeeks(6)
            if (hasLocalData && lastSynced != null && isRequestValid(lastSynced.lastSyncedAt, duration)) {
                return@withContext
            }
        }
        Timber.d("Refresh recipes for $request")
        val dtos = fetchRecipes(request, page) ?: return@withContext
        if (dtos.isEmpty()) return@withContext
        saveRemoteRecipes(dtos, page, request)
    }

    override fun getRecipes(request: SyncType, page: Int): Flow<List<Recipe>> {
        Timber.d("Observe recipes for $request and page: $page")
        return when(request) {
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

    override fun observeFavoriteRecipes(): Flow<List<Recipe>> {
        return db.recipeDAO().observeFavoriteRecipes()
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities ->
                recipesMapper.entitiesToModels(entities)
            }.flowOn(dispatcher.computation)
    }

    override fun observeSearchSuggestions(query: String): Flow<List<Recipe>> {
        return db.recipeDAO().searchFavRecipes(query)
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities ->
                recipesMapper.entitiesToModels(entities)
            }.flowOn(dispatcher.computation)
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
}