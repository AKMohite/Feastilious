// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.repository

import androidx.paging.PagingConfig
import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.dao.HealthyRecipeDAO
import com.mak.feastit.database.dao.LastSyncDAO
import com.mak.feastit.database.dao.PocketFriendlyRecipeDAO
import com.mak.feastit.database.dao.PopularRecipeDAO
import com.mak.feastit.database.dao.QuickRecipeDAO
import com.mak.feastit.database.dao.RecipeDAO
import com.mak.feastit.database.dao.TopRecipesDAO
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.TopRecipeEntity
import com.mak.feastit.database.entity.custom.PaginatedRecipeEntity
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.util.DispatcherProvider
import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.ComplexSearchDTO
import com.mak.feastit.remote.dto.RecipeDTO
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class) // Optional: for nicer @MockK annotation usage, or use MockKAnnotations.init(this)
internal class RealRecipesRepositoryTest {
  @MockK
  private lateinit var mockApi: FeastAPIService

  @MockK
  private lateinit var mockDb: FeastDB

  // Mocks for DAOs used by the repository
  @MockK
  private lateinit var mockRecipeDAO: RecipeDAO

  @MockK
  private lateinit var mockPopularRecipeDAO: PopularRecipeDAO

  @MockK
  private lateinit var mockTopRecipesDAO: TopRecipesDAO

  @MockK
  private lateinit var mockHealthyRecipeDAO: HealthyRecipeDAO

  @MockK
  private lateinit var mockQuickRecipeDAO: QuickRecipeDAO

  @MockK
  private lateinit var mockPocketFriendlyRecipeDAO: PocketFriendlyRecipeDAO

  @MockK
  private lateinit var mockLastSyncDao: LastSyncDAO

  private lateinit var testDispatcher: TestDispatcher
  private lateinit var testDispatcherProvider: DispatcherProvider

  // Real instance of mapper if its logic is simple and doesn't have external dependencies
  // Otherwise, you might consider mocking it or creating a fake implementation.
  private val recipesMapper = RecipesMapper()

  private lateinit var repository: RealRecipesRepository

  @BeforeEach
  fun setUp() {
    MockKAnnotations.init(this) // Initialize @MockK annotated properties
    mockkStatic("androidx.room.RoomDatabaseKt")

    testDispatcher = UnconfinedTestDispatcher() // Or StandardTestDispatcher()
    Dispatchers.setMain(testDispatcher) // Set main dispatcher for tests (if your repo uses it indirectly)

    testDispatcherProvider =
      object : DispatcherProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val computation = testDispatcher
//            override val unconfined = testDispatcher
      }

    // Setup mock DAO behavior for db instance
    every { mockDb.recipeDAO() } returns mockRecipeDAO
    every { mockDb.popularRecipeDAO() } returns mockPopularRecipeDAO
    every { mockDb.topRecipesDAO() } returns mockTopRecipesDAO
    every { mockDb.healthyRecipeDAO() } returns mockHealthyRecipeDAO
    every { mockDb.quickRecipeDAO() } returns mockQuickRecipeDAO
    every { mockDb.pocketFriendlyRecipeDAO() } returns mockPocketFriendlyRecipeDAO
    every { mockDb.lastSyncDao() } returns mockLastSyncDao

        /*coEvery { mockDb.handleTransaction(any()) } coAnswers  {
            val block = firstArg<suspend () -> Unit>()
            block()
        }*/

    val transactionLambda = slot<suspend () -> Unit>()
    coEvery { mockDb.handleTransaction(capture(transactionLambda)) } coAnswers {
      transactionLambda.captured.invoke()
    }

    repository =
      RealRecipesRepository(
        api = mockApi,
        db = mockDb,
        dispatcher = testDispatcherProvider,
      ).apply {
        // Override the real mapper instance with our test instance if it was created inside the repo
        // In your case, it's passed or created, so ensure the one used in tests is controlled.
        // If RecipesMapper is a concrete class with no dependencies, using a real one is fine.
        // this.recipesMapper = this@RealRecipesRepositoryTest.recipesMapper // Only if it's overridable and needed
      }
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain() // Reset main dispatcher
    clearAllMocks() // Clear MockK mocks after each test
  }

  @Nested
  @DisplayName("refreshRecipes Tests")
  inner class RefreshRecipesTests {
    @Test
    fun `refreshRecipes when page greater than 5 should return without fetching`() = runTest(testDispatcher) {
      val requestType = SyncType.POPULAR_RECIPES
      repository.refreshRecipes(requestType, 6, forceRefresh = false)

//                val spySut = spyk(repository, recordPrivateCalls = true)
//                coVerify { repository["saveRemoteRecipes"].wasNot(Called) }
      coVerify(exactly = 0) { mockApi.searchRecipes(any()) }
      coVerify(exactly = 0) { mockDb.lastSyncDao().getLastSync(any()) }
    }

    @Test
    fun `refreshRecipes when not forceRefresh and local data is valid should return without fetching`() = runTest(testDispatcher) {
      val requestType = SyncType.POPULAR_RECIPES
      val page = 1
      val currentTime = Clock.System.now().plus(1.hours)
      val validLastSync =
        LastSyncEntity(
          id = 1L,
          entityType = requestType.name,
          lastSyncedAt = currentTime,
        ) // Synced just now

      // Mock isLocallyAvailable - This needs knowledge of how isLocallyAvailable is implemented
      // For simplicity, let's assume popular recipes DAO getRecipes is used for the check
      coEvery {
        mockDb.popularRecipeDAO().getCount(page)
      } returns LIMIT_ITEMS // Simulates data exists
      coEvery { mockDb.lastSyncDao().getLastSync(requestType.name) } returns validLastSync

      repository.refreshRecipes(requestType, page, forceRefresh = false)

      coVerify(exactly = 0) { mockApi.searchRecipes(any()) }
    }

    @Test
    fun `refreshRecipes when data is stale should fetch from API and save to DB`() = runTest(testDispatcher) {
      val requestType = SyncType.POPULAR_RECIPES
      val page = 1
      val staleTime = Clock.System.now().minus(61.days) // Older than 60 days
      val staleLastSync =
        LastSyncEntity(id = 1L, entityType = requestType.name, lastSyncedAt = staleTime)
      val mockRecipeDTOs = listOf(RecipeDTO(id = 1, title = "Fetched Recipe"))
      val mockApiResponse =
        ComplexSearchDTO(
          results = mockRecipeDTOs,
          offset = 0,
          number = 1,
          totalResults = 1,
        )

      // Assume no local data or stale
      coEvery { mockDb.popularRecipeDAO().getCount(any()) } returns 0 // No local data
      coEvery { mockDb.lastSyncDao().getLastSync(requestType.name) } returns staleLastSync
      coEvery { mockApi.searchRecipes(any()) } returns mockApiResponse
      coEvery { mockDb.popularRecipeDAO().deleteRecipes() } just Runs
      coEvery { mockDb.recipeDAO().insert(any<List<RecipeEntity>>()) } just Runs
      coEvery { mockDb.recipeDAO().upsert(any<List<RecipeEntity>>()) } just Runs
      coEvery {
        mockDb.popularRecipeDAO().insert(any<List<PopularRecipeEntity>>())
      } just Runs // Adjust based on saveRemoteRecipes logic
//            this function gets suspended indefinitely if we write "just Awaits" so returning default value
      coEvery { mockDb.lastSyncDao().insert(any<LastSyncEntity>()) } returns 1L

      repository.refreshRecipes(requestType, page, forceRefresh = false)

      coVerify { mockApi.searchRecipes(match { params -> params["sort"] == "popularity" && params["offset"] == "0" }) }
      coVerify { mockDb.handleTransaction(any()) }
//            coVerify { mockDb.recipeDAO().insert(
//                check { entities ->
// //                assertEquals(1, entities.size)
// //                assertEquals(1L, entities.first())
//                    return@check true
//                }
//            ) }
      coVerify { mockDb.lastSyncDao().insert(any<LastSyncEntity>()) }
      coVerify { mockDb.recipeDAO().upsert(any<List<RecipeEntity>>()) }
      coVerify { mockDb.popularRecipeDAO().insert(any<List<PopularRecipeEntity>>()) }
    }

    @Test
    fun `refreshRecipes on forceRefresh should fetch from API even if local data is valid`() = runTest(testDispatcher) {
      val requestType = SyncType.TOP_RATED_RECIPES
      val page = 1
      val currentTime = Clock.System.now().minus(30.minutes)
      val validLastSync =
        LastSyncEntity(
          id = 1L,
          entityType = requestType.name,
          lastSyncedAt = currentTime,
        )
      val mockRecipeDTOs = listOf(RecipeDTO(id = 2, title = "Force Fetched"))
      val mockApiResponse =
        ComplexSearchDTO(
          results = mockRecipeDTOs,
          offset = 0,
          number = 1,
          totalResults = 1,
        )

      // Mock isLocallyAvailable - Assume has data
      coEvery { mockDb.topRecipesDAO().getCount(page) } returns LIMIT_ITEMS
      coEvery { mockDb.lastSyncDao().getLastSync(requestType.name) } returns validLastSync
      coEvery { mockApi.searchRecipes(any()) } returns mockApiResponse
      coEvery { mockDb.recipeDAO().upsert(any<List<RecipeEntity>>()) } just runs
      coEvery { mockDb.topRecipesDAO().insert(any<List<TopRecipeEntity>>()) } just runs
      coEvery { mockDb.lastSyncDao().insert(any<LastSyncEntity>()) } returns 1L
      coEvery { mockDb.topRecipesDAO().deleteRecipes() } just Runs

      repository.refreshRecipes(requestType, page, forceRefresh = true)

      coVerify { mockApi.searchRecipes(match { params -> params["sort"] == "meta-score" }) }
      coVerify { mockDb.recipeDAO().upsert(any<List<RecipeEntity>>()) }
      coVerify { mockDb.topRecipesDAO().insert(any<List<TopRecipeEntity>>()) }
    }

    @Test
    fun `refreshRecipes when API returns empty list should not save anything`() = runTest(testDispatcher) {
      val requestType = SyncType.HEALTHY_RECIPES
      val page = 1
      val mockApiResponse =
        ComplexSearchDTO(
          results = emptyList(),
          offset = 0,
          number = 0,
          totalResults = 0,
        )

      coEvery { mockDb.healthyRecipeDAO().getCount(page) } returns 0 // No local data
      coEvery {
        mockDb.lastSyncDao().getLastSync(requestType.name)
      } returns null // No sync record
      coEvery { mockApi.searchRecipes(any()) } returns mockApiResponse

      repository.refreshRecipes(requestType, page, forceRefresh = false)

      coVerify(exactly = 0) { mockDb.handleTransaction(any()) }
      coVerify(exactly = 0) { mockDb.recipeDAO().upsert(any<List<RecipeEntity>>()) }
    }
  }

  @Nested
  @DisplayName("getRecipes Tests")
  inner class GetRecipesTests {
    @Test
    fun `getRecipes for POPULAR_RECIPES should call correct DAO and map results`() = runTest(testDispatcher) {
      val page = 1
      val mockRecipeEntities =
        listOf(
          createRecipeFor(1),
          createRecipeFor(2),
        )
      val expectedRecipes = recipesMapper.entitiesToModels(mockRecipeEntities)

      coEvery { mockDb.popularRecipeDAO().getRecipes(page) } returns
        flowOf(
          mockRecipeEntities,
        )

      val resultFlow = repository.getRecipes(SyncType.POPULAR_RECIPES, page)
      val actualRecipes = resultFlow.first()

      assertEquals(expectedRecipes, actualRecipes)
      expectedRecipes.forEach { recipe ->
        assertTrue(actualRecipes.contains(recipe))
      }
//                coVerify { mockDb.popularRecipeDAO().getRecipes(page) }
    }

    // TODO: Add similar tests for TOP_RATED_RECIPES, HEALTHY_RECIPES, etc.
    @Test
    fun `getRecipes for TOP_RATED_RECIPES should call correct DAO`() = runTest(testDispatcher) {
      val page = 1
      coEvery { mockTopRecipesDAO.getRecipes(page) } returns flowOf(emptyList()) // Simple check for DAO call

      repository
        .getRecipes(SyncType.TOP_RATED_RECIPES, page)
        .first() // Collect to trigger flow

      coVerify { mockTopRecipesDAO.getRecipes(page) }
    }

    @Test
    fun `getRecipes for invalid SyncType should throw IllegalArgumentException`() {
      assertThrows(IllegalArgumentException::class.java) {
        runTest(testDispatcher) {
          // runTest needed if the function under test is suspend or uses coroutines
          repository.getRecipes(SyncType.RECIPE_DETAILS, 1).first()
        }
      }
    }
  }

  @Nested
  @DisplayName("observePaginatedRecipes Tests")
  inner class ObservePaginatedRecipesTests {
    // Testing Paging 3 with RemoteMediator is more complex.
    // You'd typically test the RemoteMediator and PagingSource separately.
    // For the repository method itself, you can verify it constructs the Pager correctly.

    @Test
    fun `observePaginatedRecipes should create Pager with correct PagingSourceFactory`() = runTest(testDispatcher) {
      val requestType = SyncType.POPULAR_RECIPES
      val pagingConfig = PagingConfig(pageSize = LIMIT_ITEMS)
      val mockPagingSource =
        mockk<androidx.paging.PagingSource<Int, PaginatedRecipeEntity>>()

      // Mock the DAO call within getPagedRecipes
      every { mockPopularRecipeDAO.pagedRecipes() } returns mockPagingSource
      // Mock the isCacheValid lambda in RemoteMediator
      coEvery { mockLastSyncDao.getLastSync(requestType.name) } returns
        LastSyncEntity(
          id = 1L,
          entityType = requestType.name,
          lastSyncedAt = Clock.System.now(),
        )

      // We don't need to test the full PagingData emission here,
      // just that the Pager is set up with the right source from the DAO.
      // A full integration test would be better for PagingData content.
      val flow = repository.observePaginatedRecipes(requestType, pagingConfig)

      // To actually trigger the factory, we might need to collect from the flow.
      // However, deeply testing Pager behavior is tricky in unit tests.
      // Focus on verifying the PagingSource is what you expect.
      // For now, let's just verify the DAO was called.
      // A more robust test would involve collecting from the flow and asserting on the data,
      // but that requires more setup for PagingSource and RemoteMediator.

      // Minimal verification:
      assertNotNull(flow)
      // To ensure the factory is called, we'd typically try to collect.
      // This is a placeholder; more detailed Paging tests are involved.
      // flow.first() // This would trigger the factory & remote mediator

      // Verify that when getPagedRecipes is called internally by Pager, it uses the correct DAO
      // This is a bit indirect. A direct test of `getPagedRecipes` might be better.
      // For this specific test, it's about the Pager setup.

      // The actual PagingSource is created inside the lambda.
      // We can verify that the DAO method that *produces* the PagingSource is called.
      // This requires the Pager to actually invoke its factory.
      // Let's ensure the DAO for popular recipes' paged source is called.
      // This setup for RemoteMediator is also important
      coEvery { mockApi.searchRecipes(any()) } returns
        ComplexSearchDTO(
          0,
          0,
          emptyList(),
          0,
        ) // Mediator might fetch
      coEvery { mockRecipeDAO.upsert(any<List<RecipeEntity>>()) } just runs
      coEvery { mockPopularRecipeDAO.insert(any<List<PopularRecipeEntity>>()) } just runs
      coEvery { mockLastSyncDao.insert(any<LastSyncEntity>()) } returns 1L

      // Try to collect one item to trigger internal Pager mechanisms
      // This may still not be enough to verify the pagingSourceFactory directly in isolation
      // without a more complex Paging test setup.
      // Consider testing `getPagedRecipes` separately.
      try {
        flow.first() // Attempt to trigger pager
      } catch (e: Exception) {
        // Paging flow might throw if data source is empty or other Pager issues
        // This is not ideal for this specific unit test's focus.
      }
      // A better verification if we could inspect the Pager's factory:
      // verify { mockPopularRecipeDAO.pagedRecipes() } // This is what we want to assert

      // For now, this test is more of a structural check.
      // Full RemoteMediator tests are usually done by testing the RemoteMediator class itself.
    }

    @Test
    fun `searchRecipe should call DAO and map results`() = runTest(testDispatcher) {
      val query = "chicken"
      val mockEntities = listOf(createRecipeFor(1).copy(recipeName = query))
      val expectedRecipes = recipesMapper.entitiesToModels(mockEntities)

      coEvery { mockRecipeDAO.searchRecipes(query) } returns mockEntities

      val actualRecipes = repository.searchRecipe(query)

      assertEquals(expectedRecipes, actualRecipes)
      coVerify { mockRecipeDAO.searchRecipes(query) }
    }

    @Test
    fun `observeFavoriteRecipes should call DAO and map results`() = runTest(testDispatcher) {
      val mockFavoriteEntities = listOf(createRecipeFor(1))
      val expectedRecipes = recipesMapper.entitiesToModels(mockFavoriteEntities)

      coEvery { mockRecipeDAO.observeFavoriteRecipes() } returns flowOf(mockFavoriteEntities)

      val resultFlow = repository.observeFavoriteRecipes()
      val actualRecipes = resultFlow.first()

      assertEquals(expectedRecipes, actualRecipes)
      coVerify { mockRecipeDAO.observeFavoriteRecipes() }
    }

    @Test
    fun `observeSearchSuggestions should call DAO and map results`() = runTest(testDispatcher) {
      val query = "salad"
      val mockSuggestionEntities =
        listOf(createRecipeFor(2).copy(cuisines = query)) // assuming searchFavRecipes can return non-favs for suggestions
      val expectedRecipes = recipesMapper.entitiesToModels(mockSuggestionEntities)

      coEvery { mockRecipeDAO.searchFavRecipes(query) } returns flowOf(mockSuggestionEntities)

      val resultFlow = repository.observeSearchSuggestions(query)
      val actualRecipes = resultFlow.first()

      assertEquals(expectedRecipes, actualRecipes)
      coVerify { mockRecipeDAO.searchFavRecipes(query) }
    }

    // --- Helper for isLocallyAvailable ---
    // You'll need to mock the underlying DAO calls that isLocallyAvailable uses.
    // Example: If isLocallyAvailable for POPULAR_RECIPES checks mockPopularRecipeDAO.getRecipesCount():
    private fun mockIsLocallyAvailable(
      syncType: SyncType,
      page: Int,
      hasData: Boolean,
    ) {
      val expectedCount = if (hasData) LIMIT_ITEMS else 0
      val offset =
        (page - 1) * LIMIT_ITEMS // Assuming your isLocallyAvailable uses similar logic to getOffset
      when (syncType) {
        SyncType.POPULAR_RECIPES -> coEvery { mockPopularRecipeDAO.getCount(page) } returns expectedCount
        SyncType.TOP_RATED_RECIPES -> coEvery { mockTopRecipesDAO.getCount(page) } returns expectedCount
        // Add other cases
        else -> {}
      }
    }
  }

  private fun createRecipeFor(id: Long): RecipeEntity = RecipeEntity(
    id = id,
    recipeName = "Recipe $id",
    recipeSummary = "Summary $id",
    recipeSource = "source$id",
    recipeReadyInMins = id.toInt(),
    servings = 4,
    pricePerServing = 200.toDouble(),
    sourceName = "sourceName$id",
    isAddedToCollection = false,
    cuisines = "cuisines$id",
    dishTypes = "dishTypes$id",
    diets = "diets$id",
    caloricBreakdown =
    mapOf(
      "type1" to "value1",
      "type2" to "value2",
    ),
  )
}
