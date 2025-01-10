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
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.minus
import kotlinx.datetime.offsetAt
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

const val LIMIT_ITEMS = 20

internal class RealRecipesRepository @Inject constructor(
    private val api: FeastAPIService,
    private val db: FeastDB,
    private val dispatcher: DispatcherProvider
): BaseRecipeRepository(
    db = db
) {

    override val recipesMapper = RecipesMapper()

//    meal planner date range
    private fun dateRange() {
//        val today = Calendar.getInstance()
//        val startOfWeek = today.apply {
//            firstDayOfWeek = Calendar.SUNDAY
//            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
//        }.time
//
//        val endOfWeek = today.apply {
//            firstDayOfWeek = Calendar.SUNDAY
//            set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
//        }.time
//        Timber.d("Start of week: $startOfWeek")
//        Timber.d("End of week: $endOfWeek")

        val now = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()
//    timeZone.offsetAt(now)
    val toLocalDateTime = now.toLocalDateTime(timeZone)
    Timber.d("Local date: $toLocalDateTime")
//    DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
    val europeTimeZone = TimeZone.of("Europe/Paris")
    Timber.d("Europe date: ${now.toLocalDateTime(europeTimeZone)}")
    val today = toLocalDateTime.date
        val days = mutableListOf<LocalDate>()
        val firstWeekDay = today.daysShift(-DayOfWeek.entries.indexOf(today.dayOfWeek))
        for (i in 0 until DayOfWeek.entries.toTypedArray().count()) {
            days.add(firstWeekDay.daysShift(i))
        }
        val dayStrings = days.map { "${it.dayOfWeek}, ${it.dayOfMonth}" }
        Timber.d("$dayStrings")

    }

    fun LocalDate.daysShift(days: Int): LocalDate = when {
        days < 0 -> {
            minus(1, DateTimeUnit.DayBased(-days))
        }
        days > 0 -> {
            plus(1, DateTimeUnit.DayBased(days))
        }
        else -> this
    }

    override suspend fun refreshRecipes(request: SyncType, page: Int, forceRefresh: Boolean) = withContext(dispatcher.io) {
        dateRange()
        if (page > 5) return@withContext // TODO Use pro just have limited API calls condition can be removed
        if (!forceRefresh) {
            val hasLocalData = isLocallyAvailable(page, request)
            val lastSynced = db.lastSyncDao().getLastSync(request.name)
//        TODO validity duration can be less but for now kept 6hours
            if (hasLocalData && lastSynced != null && isRequestValid(lastSynced.lastSyncedAt.toInstant(TimeZone.currentSystemDefault()), 60.days)) {
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
        Timber.d("Search for $query")
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