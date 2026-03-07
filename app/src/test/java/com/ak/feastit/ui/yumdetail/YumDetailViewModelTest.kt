// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.core.notification.NotificationManager
import com.ak.feastit.worker.WorkerScheduler
import com.mak.feastit.domain.model.ImageType
import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.model.RecipeImageSize
import com.mak.feastit.domain.model.YumNotification
import com.mak.feastit.domain.model.YumNotificationChannel
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.usecase.RecipeDetailIngredientsUsecase
import com.mak.feastit.domain.util.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlin.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class YumDetailViewModelTest {

  @MockK
  private lateinit var recipeRepository: RecipeRepository

  @MockK
  private lateinit var cartRepository: CartRepository

  @MockK
  private lateinit var mealPlanRepository: MealPlanRepository

  @MockK
  private lateinit var getIngredients: RecipeDetailIngredientsUsecase

  @MockK
  private lateinit var scheduler: WorkerScheduler

  @MockK
  private lateinit var notification: NotificationManager

  private lateinit var testDispatcher: TestDispatcher
  private lateinit var dispatcherProvider: DispatcherProvider
  private lateinit var savedStateHandle: SavedStateHandle

  private lateinit var viewModel: YumDetailViewModel

  private val recipeId = 123L

  @BeforeEach
  fun setUp() {
    MockKAnnotations.init(this)
    testDispatcher = UnconfinedTestDispatcher()
    Dispatchers.setMain(testDispatcher)

    dispatcherProvider = object : DispatcherProvider {
      override val main = testDispatcher
      override val io = testDispatcher
      override val computation = testDispatcher
    }

    savedStateHandle = SavedStateHandle(mapOf(SAVED_RECIPE_ID to recipeId))

    // Default mocks to avoid crashes during init
    coEvery { recipeRepository.refreshRecipe(any(), any()) } just Runs
    coEvery { recipeRepository.refreshAnalyzedInstruction(any(), any()) } just Runs
    coEvery { recipeRepository.refreshSimilarRecipes(any(), any()) } just Runs

    every { recipeRepository.observerRecipe(recipeId) } returns flowOf(createMockRecipeDetail())
    every { recipeRepository.observerSimilarRecipes(recipeId) } returns flowOf(emptyList())
    every { recipeRepository.observeNutrients(recipeId) } returns flowOf(emptyList())
    every { recipeRepository.observeInstructions(recipeId) } returns flowOf(emptyList())
    every { getIngredients(recipeId) } returns flowOf(emptyList())
    every { mealPlanRepository.hasRecipe(recipeId) } returns flowOf(false)

    viewModel = YumDetailViewModel(
      recipeRepository,
      cartRepository,
      mealPlanRepository,
      getIngredients,
      scheduler,
      notification,
      dispatcherProvider,
      savedStateHandle
    )
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `init should throw exception if recipeId is missing`() {
    assertThrows(IllegalArgumentException::class.java) {
      YumDetailViewModel(
        recipeRepository,
        cartRepository,
        mealPlanRepository,
        getIngredients,
        scheduler,
        notification,
        dispatcherProvider,
        SavedStateHandle()
      )
    }
  }

  @Test
  fun `init should refresh recipe info and observe recipe`() = runTest {
    coVerify { recipeRepository.refreshRecipe(recipeId, false) }
    coVerify { recipeRepository.refreshAnalyzedInstruction(recipeId, false) }
    coVerify { recipeRepository.refreshSimilarRecipes(recipeId, false) }
    coVerify { recipeRepository.observerRecipe(recipeId) }
  }

  @Test
  fun `toggleFavorite should call repository toggleFavorite`() = runTest {
    coEvery { recipeRepository.toggleFavorite(recipeId) } just Runs

    viewModel.toggleFavorite()

    coVerify { recipeRepository.toggleFavorite(recipeId) }
  }

  @Test
  fun `toggleCart should call repository toggleShoppingAllIngredientsForRecipe`() = runTest {
    coEvery { cartRepository.toggleShoppingAllIngredientsForRecipe(recipeId) } just Runs

    viewModel.toggleCart()

    coVerify { cartRepository.toggleShoppingAllIngredientsForRecipe(recipeId) }
  }

  @Test
  fun `toggleIngredientCart should call repository toggleShoppingIngredient`() = runTest {
    val ingredientId = "ing_1"
    coEvery { cartRepository.toggleShoppingIngredient(ingredientId) } just Runs

    viewModel.toggleIngredientCart(ingredientId)

    coVerify { cartRepository.toggleShoppingIngredient(ingredientId) }
  }

  @Test
  fun `toggleMealPlan should call mealPlanRepository toggleMealPLanFor`() = runTest {
    coEvery { mealPlanRepository.getMealPlansForRecipe(recipeId) } returns emptyList()
    coEvery { mealPlanRepository.toggleMealPLanFor(recipeId) } returns true

    viewModel.toggleMealPlan()

    coVerify { mealPlanRepository.toggleMealPLanFor(recipeId) }
  }

  @Test
  fun `toggleMealPlan when removing should cancel scheduler and notifications`() = runTest {
    val mealPlan = MealPlanRecipe(
      id = 1L,
      recipeId = recipeId,
      scheduledFor = LocalDateTime(2025, 1, 1, 12, 0),
      preparationTime = 30,
      isMade = false,
      name = "Mock Recipe",
      image = null
    )
    val mockNotification = YumNotification(
      id = "meal-plan-1-$recipeId",
      title = "",
      message = "",
      channel = YumNotificationChannel.MEAL_PLANNING,
      image = null,
      dateTime = Instant.fromEpochMilliseconds(0)
    )

    coEvery { mealPlanRepository.getMealPlansForRecipe(recipeId) } returns listOf(mealPlan)
    coEvery { mealPlanRepository.toggleMealPLanFor(recipeId) } returns false
    coEvery { scheduler.cancelMealWorker(1L) } just Runs
    coEvery { notification.cancelAll(any()) } just Runs

    viewModel.toggleMealPlan()

    coVerify { scheduler.cancelMealWorker(1L) }
    coVerify { notification.cancelAll(match { it.first().id == mockNotification.id }) }
  }

  @Test
  fun `state should update when repository emits recipe detail`() = runTest {
    val recipeDetail = createMockRecipeDetail()
    every { recipeRepository.observerRecipe(recipeId) } returns flowOf(recipeDetail)

    // Re-init to trigger observeRecipe with mock
    viewModel = YumDetailViewModel(
      recipeRepository,
      cartRepository,
      mealPlanRepository,
      getIngredients,
      scheduler,
      notification,
      dispatcherProvider,
      savedStateHandle
    )

    assertEquals(recipeDetail, viewModel.state.value.overview)
  }

  /*@Test
  fun `ingredient header should show remove if any ingredient is in cart`() = runTest {
    val ingredient = Ingredient(
      id = "1",
      image = "img",
      localizedName = "Sugar",
      name = "Sugar",
      quantity = "1 kg",
      isInCart = true
    )
    every { getIngredients(recipeId) } returns flowOf(listOf(ingredient))

    viewModel = YumDetailViewModel(
      recipeRepository,
      cartRepository,
      mealPlanRepository,
      getIngredients,
      scheduler,
      notification,
      dispatcherProvider,
      savedStateHandle
    )

    val sections = viewModel.state.value.ingredientSections
    assertTrue(sections.any { it is IngredientSection.Header && it.title == R.string.remove_ingredients_from_shopping_list })
  }*/

  /*@Test
  fun `instruction header should show remove if recipe is in meal plan`() = runTest {
    every { mealPlanRepository.hasRecipe(recipeId) } returns flowOf(true)

    viewModel = YumDetailViewModel(
      recipeRepository,
      cartRepository,
      mealPlanRepository,
      getIngredients,
      scheduler,
      notification,
      dispatcherProvider,
      savedStateHandle
    )

    val instructions = viewModel.state.value.instructions
    assertTrue(instructions.any { it is StepSection.Header && it.title == R.string.recipe_remove_from_meal_plan })
  }*/

  /*@Test
  fun `instruction header should show add if recipe is not in meal plan`() = runTest {
    every { mealPlanRepository.hasRecipe(recipeId) } returns flowOf(false)

    viewModel = YumDetailViewModel(
      recipeRepository,
      cartRepository,
      mealPlanRepository,
      getIngredients,
      scheduler,
      notification,
      dispatcherProvider,
      savedStateHandle
    )

    val instructions = viewModel.state.value.instructions
    assertTrue(instructions.any { it is StepSection.Header && it.title == R.string.recipe_add_to_meal_plan })
  }*/

  private fun createMockRecipeDetail() = RecipeDetail(
    recipeId = recipeId,
    recipeName = "Mock Recipe",
    recipeSummary = "Summary",
    recipeImg = RecipeImage(recipeId, "jpg", RecipeImageSize.MEDIUM, ImageType.CELL),
    recipeSource = "Source",
    recipeReadyInMins = 30,
    servings = 4,
    pricePerServing = 2.5,
    sourceName = "Source Name"
  )
}
