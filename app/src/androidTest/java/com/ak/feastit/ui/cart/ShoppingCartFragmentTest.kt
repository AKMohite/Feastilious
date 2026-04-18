package com.ak.feastit.ui.cart

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ak.feastit.R
import com.ak.feastit.launchFragmentInHiltContainer
import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.ImageType
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.model.RecipeImageSize
import com.mak.feastit.domain.repository.CartRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ShoppingCartFragmentTest {

  @get:Rule(order = 0)
  var hiltRule = HiltAndroidRule(this)

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
    val ingredient = CartIngredient(
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
    val cartIngredients = listOf(
      ingredient
    )
    fakeRepo.emit(cartIngredients)

    val context = ApplicationProvider.getApplicationContext<Context>()
    val servings = context.getString(R.string.servings, ingredient.servings)
    launchFragmentInHiltContainer<ShoppingCartFragment>()

    onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
    onView(withText(ingredient.recipeName)).check(matches(isDisplayed()))
    onView(withText("${ingredient.quantity} ${ingredient.ingredientName}")).check(matches(isDisplayed()))
    onView(withText(servings)).check(matches(isDisplayed()))
  }
}
