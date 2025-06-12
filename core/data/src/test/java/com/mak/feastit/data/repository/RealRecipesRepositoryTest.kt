package com.mak.feastit.data.repository

import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.ComplexSearchDTO
import com.mak.feastit.remote.dto.RecipeDTO
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

/*
dependencies {
    // ... other dependencies
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3" // Or latest
    testImplementation "org.mockito.kotlin:mockito-kotlin:5.1.0" // Or latest
    testImplementation "androidx.arch.core:core-testing:2.2.0" // For InstantTaskExecutorRule if using LiveData
    testImplementation "junit:junit:4.13.2"
}
* */

/*@ExperimentalCoroutinesApi
class RealRecipesRepositoryTest {

    private lateinit var repository: RealRecipesRepository
    private lateinit var mockApi: FeastAPIService
    private lateinit var mockDb: FeastDB
    private lateinit var mockRecipeDao: RecipeDao // Assuming you have a general RecipeDao
    private lateinit var mockLastSyncDao: LastSyncDao
    private lateinit var testDispatcher: DispatcherProvider

    private val recipesMapper = RecipesMapper() // Real instance, or mock if it has complex logic

    @Before
    fun setUp() {
        mockApi = mock()
        mockDb = mock()
        mockRecipeDao = mock()
        mockLastSyncDao = mock()

        // Mock DAO access from mockDb
        whenever(mockDb.recipeDao()).thenReturn(mockRecipeDao) // Adjust if BaseRecipeRepository uses other DAOs
        whenever(mockDb.lastSyncDao()).thenReturn(mockLastSyncDao)
        // Mock individual DAOs used by getRecipes/isLocallyAvailable if BaseRecipeRepository doesn't abstract them well enough
        // For simplicity, I'm assuming BaseRecipeRepository handles the specific DAO interactions for isLocallyAvailable

        val dispatcher = StandardTestDispatcher()
        testDispatcher = object : DispatcherProvider {
            override val main = dispatcher
            override val io = dispatcher
            override val computation = dispatcher
            override val unconfined = dispatcher
        }

        repository = RealRecipesRepository(mockApi, mockDb, testDispatcher)
        repository.recipesMapper = recipesMapper // Inject real or mock mapper
    }

    @Test
    fun `refreshRecipes WHEN page > 5 THEN returns early`() = runTest {
        repository.refreshRecipes(SyncType.POPULAR_RECIPES, 6, false)

        verifyNoInteractions(mockApi)
        verifyNoInteractions(mockDb.recipeDao()) // Or specific DAOs
        verifyNoInteractions(mockLastSyncDao)
    }

    @Test
    fun `refreshRecipes WHEN forceRefresh is false AND no local data THEN fetches from API`() = runTest {
        val requestType = SyncType.TOP_RATED_RECIPES
        val page = 1

        val mockTopRatedDao: com.mak.feastit.database.dao.TopRatedRecipeDAO = mock()
        whenever(mockDb.topRecipesDAO()).thenReturn(mockTopRatedDao)
        whenever(mockTopRatedDao.getRecipesCountForPage(page)).thenReturn(0) // No local data

        whenever(mockLastSyncDao.getLastSync(requestType.name)).thenReturn(null) // No sync record

        val mockRecipeDTO = RecipeDTO(id = 2, title = "Top Recipe")
        val mockApiResponse = RecipesResponseDTO(results = listOf(mockRecipeDTO), offset = 0, number = 1, totalResults = 1)
        whenever(mockApi.searchRecipes(any())).thenReturn(mockApiResponse)

        repository.refreshRecipes(requestType, page, false)

        verify(mockApi).searchRecipes(argThat {
            this["sort"] == "meta-score"
        })
        verify(mockTopRatedDao).deleteRecipesForPage(page)
        verify(mockRecipeDao).insertRecipes(any())
        verify(mockTopRatedDao).insertAll(any())
        verify(mockLastSyncDao).insert(any())
    }

    @Test
    fun `refreshRecipes WHEN forceRefresh is true THEN fetches from API regardless of cache`() = runTest {
        val requestType = SyncType.HEALTHY_RECIPES
        val page = 1

        // Even if local data exists and is recent, forceRefresh should bypass checks
        val mockHealthyDao: com.mak.feastit.database.dao.HealthyRecipeDAO = mock()
        whenever(mockDb.healthyRecipeDAO()).thenReturn(mockHealthyDao)
        // No need to mock getRecipesCountForPage or getLastSync for this path

        val mockRecipeDTO = RecipeDTO(id = 3, title = "Healthy Recipe")
        val mockApiResponse = RecipesResponseDTO(results = listOf(mockRecipeDTO), offset = 0, number = 1, totalResults = 1)
        whenever(mockApi.searchRecipes(any())).thenReturn(mockApiResponse)

        repository.refreshRecipes(requestType, page, true)

        verify(mockApi).searchRecipes(argThat {
            this["sort"] == "healthiness"
        })
        verify(mockHealthyDao).deleteRecipesForPage(page)
        verify(mockRecipeDao).insertRecipes(any())
        verify(mockHealthyDao).insertAll(any())
        verify(mockLastSyncDao).insert(any())
        verify(mockLastSyncDao, never()).getLastSync(any()) // Ensure cache checks are skipped
        verify(mockHealthyDao, never()).getRecipesCountForPage(any()) // Ensure cache checks are skipped
    }

    @Test
    fun `refreshRecipes WHEN API returns null dtos THEN returns early`() = runTest {
        val requestType = SyncType.QUICK_RECIPES
        val page = 1

        whenever(mockLastSyncDao.getLastSync(requestType.name)).thenReturn(null) // Force API call
        val mockQuickDao: com.mak.feastit.database.dao.QuickRecipeDAO = mock()
        whenever(mockDb.quickRecipeDAO()).thenReturn(mockQuickDao)
        whenever(mockQuickDao.getRecipesCountForPage(page)).thenReturn(0)

        whenever(mockApi.searchRecipes(any())).thenReturn(null) // API returns null

        repository.refreshRecipes(requestType, page, false)

        verify(mockApi).searchRecipes(any())
        verify(mockDb.recipeDao(), never()).insertRecipes(any())
        verify(mockQuickDao, never()).insertAll(any())
        verify(mockLastSyncDao, never()).insert(any())
    }

    @Test
    fun `refreshRecipes WHEN API returns empty dtos THEN returns early`() = runTest {
        val requestType = SyncType.POCKET_FRIENDLY_RECIPES
        val page = 1

        whenever(mockLastSyncDao.getLastSync(requestType.name)).thenReturn(null) // Force API call
        val mockPocketFriendlyDao: com.mak.feastit.database.dao.PocketFriendlyRecipeDAO = mock()
        whenever(mockDb.pocketFriendlyRecipeDAO()).thenReturn(mockPocketFriendlyDao)
        whenever(mockPocketFriendlyDao.getRecipesCountForPage(page)).thenReturn(0)


        val mockApiResponse = RecipesResponseDTO(results = emptyList(), offset = 0, number = 0, totalResults = 0)
        whenever(mockApi.searchRecipes(any())).thenReturn(mockApiResponse)

        repository.refreshRecipes(requestType, page, false)

        verify(mockApi).searchRecipes(any())
        verify(mockDb.recipeDao(), never()).insertRecipes(any())
        verify(mockPocketFriendlyDao, never()).insertAll(any())
        verify(mockLastSyncDao, never()).insert(any())
    }

    @Test
    fun `refreshRecipes WHEN successful fetch THEN saves recipes and updates sync time`() = runTest {
        val requestType = SyncType.POPULAR_RECIPES
        val page = 1

        whenever(mockLastSyncDao.getLastSync(requestType.name)).thenReturn(null) // Force API call
        val mockPopularDao: com.mak.feastit.database.dao.PopularRecipeDAO = mock()
        whenever(mockDb.popularRecipeDAO()).thenReturn(mockPopularDao)
        whenever(mockPopularDao.getRecipesCountForPage(page)).thenReturn(0)

        val recipe1DTO = RecipeDTO(id = 101, title = "Fetched Recipe 1")
        val recipe2DTO = RecipeDTO(id = 102, title = "Fetched Recipe 2")
        val mockApiResponse = RecipesResponseDTO(results = listOf(recipe1DTO, recipe2DTO), offset = 0, number = 2, totalResults = 2)
        whenever(mockApi.searchRecipes(any())).thenReturn(mockApiResponse)

        repository.refreshRecipes(requestType, page, false)

        verify(mockApi).searchRecipes(any())

        // Verify saveRemoteRecipes interactions
        // 1. Delete old recipes for the page
        verify(mockPopularDao).deleteRecipesForPage(page)

        // 2. Insert recipes into the main Recipe table
        verify(mockRecipeDao).insertRecipes(check { entities ->
            kotlin.test.assertEquals(2, entities.size)
            kotlin.test.assertTrue(entities.any { it.remoteId == 101L && it.title == "Fetched Recipe 1" })
            kotlin.test.assertTrue(entities.any { it.remoteId == 102L && it.title == "Fetched Recipe 2" })
        })

        // 3. Insert into the specific sync type table (e.g., PopularRecipeEntity)
        verify(mockPopularDao).insertAll(check { popularRecipeEntities ->
            kotlin.test.assertEquals(2, popularRecipeEntities.size)
            kotlin.test.assertTrue(popularRecipeEntities.any { it.recipeId == 101L && it.page == page })
            kotlin.test.assertTrue(popularRecipeEntities.any { it.recipeId == 102L && it.page == page })
        })

        // 4. Update last sync time
        verify(mockLastSyncDao).insert(check { lastSyncEntity ->
            kotlin.test.assertEquals(requestType.name, lastSyncEntity.type)
            // We can't check exact timestamp easily, but we can ensure it's called
        })
    }

    // Helper method to getOffset (should match the one in your repository or BaseRecipeRepository)
    private fun getOffset(page: Int): String {
        return ((page - 1) * LIMIT_ITEMS).toString()
    }
}*/


class RealRecipesRepositoryTest {

//    TODO instead of mocking object create mock server
    @MockK
    private lateinit var api: FeastAPIService

    @MockK
    private lateinit var db: FeastDB

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: DispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher
            get() = UnconfinedTestDispatcher()
        override val computation: CoroutineDispatcher
            get() = UnconfinedTestDispatcher()
        override val main: CoroutineDispatcher
            get() = UnconfinedTestDispatcher()
    }

    private val sut = RealRecipesRepository(
        api = api,
        db = db,
        dispatcher = dispatcher
    )

    @BeforeEach
    fun before() {
        clearAllMocks()
    }
    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            MockKAnnotations.init()
        }
    }

    @Test
    @DisplayName("Get recipes for category and store in local database")
    fun refreshRecipesSuccess() = runTest {
        coEvery { api.searchRecipes(any()) } returns ComplexSearchDTO()

        sut.refreshRecipes(SyncType.POPULAR_RECIPES, 1, false)

        coVerifyOrder {
//            sut.fetchRecipes()
            api.searchRecipes(any())
//            sut.saveRemoteRecipes()
        }
    }

    @Test
    fun `refreshRecipes WHEN forceRefresh is false AND local data is available AND was recently added THEN returns early`() = runTest {
//        Given
        val requestType = SyncType.POPULAR_RECIPES
        val page = 1
        val currentTime = Clock.System.now()
        val recentSyncTime = currentTime.minus(1.hours) // Within 60 days
        coEvery { db.popularRecipeDAO().getCount(page) } returns 1
        coEvery { db.lastSyncDao().getLastSync(requestType.name) } returns LastSyncEntity(id = 1L, entityType = requestType.name, lastSyncedAt = recentSyncTime)
//        val spySut = spyk(sut, recordPrivateCalls = true)

//        When
        sut.refreshRecipes(requestType, page, false)

//        Then
//        coVerify(exactly = 0) { spySut.fetchRecipes(requestType, page) }
//        coVerify { spySut.saveRemoteRecipes(any(), any(), any()) wasNot Called }
//        coVerify(exactly = 0) { spySut.saveRemoteRecipes(any(), any(), any()) }
        coVerify { api.searchRecipes(any()) wasNot Called }
        coVerify(exactly = 0) { db.handleTransaction(any()) }
    }

    @Test
    fun `refreshRecipes WHEN forceRefresh is false AND local data available BUT stale THEN fetches from API`() = runTest {
        val requestType = SyncType.POPULAR_RECIPES
        val page = 1
        val currentTime = Clock.System.now()
        val staleSyncTime = currentTime.minus(70.days) // Older than 60 days
        val apiResponse = ComplexSearchDTO(results = listOf(
            RecipeDTO(id = 1, title = "Test Recipe 1"),
            RecipeDTO(id = 2, title = "Test Recipe 2")
        ), offset = 0, number = 1, totalResults = 2)
        coEvery { db.popularRecipeDAO().getCount(page) } returns 1
        coEvery { db.lastSyncDao().getLastSync(requestType.name) } returns LastSyncEntity(id = 1L, entityType = requestType.name, lastSyncedAt = staleSyncTime)
        coEvery {api.searchRecipes(any()) } returns apiResponse

        sut.refreshRecipes(requestType, page, false)

        coVerifyOrder {
            api.searchRecipes(any())
            db.handleTransaction(any())
        }
//        verify(mockApi).searchRecipes(argThat {
//            this["number"] == LIMIT_ITEMS.toString() &&
//                    this["offset"] == repository.getOffset(page) &&
//                    this["sort"] == "popularity"
//        })
//        // Verify saveRemoteRecipes was called (mock its interactions)
//        // This depends heavily on your saveRemoteRecipes implementation.
//        // Assuming it clears old and inserts new.
//        verify(mockPopularDao).deleteRecipesForPage(page)
//        verify(mockRecipeDao).insertRecipes(check {
//            kotlin.test.assertEquals(1, it.size)
//            kotlin.test.assertEquals("Test Recipe", it.first().title)
//        })
//        verify(mockPopularDao).insertAll(check {
//            kotlin.test.assertEquals(1, it.size)
//            kotlin.test.assertEquals(1, it.first().recipeId)
//        })
//        verify(mockLastSyncDao).insert(check {
//            kotlin.test.assertEquals(requestType.name, it.type)
//        })
    }
}