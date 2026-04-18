package com.ak.feastit.ui.cart

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.ak.feastit.HiltTestActivity
import com.ak.feastit.R
import com.ak.feastit.launchFragmentInHiltContainer
import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.ImageType
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.model.RecipeImageSize
import com.mak.feastit.domain.repository.CartRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ShoppingCartFragmentTest {

  @get:Rule(order = 0)
  var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  var activityRule: ActivityScenarioRule<HiltTestActivity> =
    ActivityScenarioRule(HiltTestActivity::class.java)

  @Inject
  lateinit var repository: CartRepository

  private val fakeRepo get() = repository as FakeCartRepository

  @Before
  fun init() {
    hiltRule.inject()
  }

  @Test
  fun displayEmptyState_whenCartIsEmpty() {
    fakeRepo.emit(emptyList())

    launchFragmentInHiltContainer<ShoppingCartFragment>()

    onView(withId(R.id.empty_state)).check(matches(isDisplayed()))
    onView(withId(R.id.cart_items)).check(matches(not(isDisplayed())))
  }

  @Test
  fun displayIngredients_whenCartHasItems() {
    val ingredients = listOf(
      CartIngredient(
        id = "1",
        isBought = false,
        aisleCategory = "Veg",
        ingredientName = "Tomato",
        recipeId = 101,
        quantity = "2",
        recipeName = "Pasta",
        recipeImg = RecipeImage(
          101,
          "jpg",
          RecipeImageSize.SMALL,
          ImageType.CELL
        ),
        servings = 2
      )
    )
    fakeRepo.emit(ingredients)

    launchFragmentInHiltContainer<ShoppingCartFragment>()

    onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
    onView(withText("Pasta")).check(matches(isDisplayed()))
    onView(withText("Tomato")).check(matches(isDisplayed()))
  }
}
