package com.ak.feastit.ui.cart

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.ak.feastit.R
import com.ak.feastit.launchFragmentInHiltContainer
import com.ak.feastit.ui.cart.component.ComponentCartIngredient
import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.ImageType
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.model.RecipeImageSize
import com.mak.feastit.domain.repository.CartRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
    val ingredient = shoppingIngredients()
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

  @Test
  fun displayIngredientsByAisle_whenCartHasItems() {
    val ingredient = shoppingIngredients()
    val cartIngredients = listOf(
      ingredient
    )
    fakeRepo.emit(cartIngredients)

    launchFragmentInHiltContainer<ShoppingCartFragment>()

    onView(withId(R.id.group_by_aisle)).perform(click())

    onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
    onView(withText(ingredient.recipeName)).check(doesNotExist())
    onView(withText(ingredient.aisleCategory)).check(matches(isDisplayed()))
    onView(withText("${ingredient.quantity} ${ingredient.ingredientName}")).check(matches(isDisplayed()))
  }

  private fun shoppingIngredients(
    id: String = "1",
    recipeId: Long = 101,
    isBought: Boolean = false
  ): CartIngredient = CartIngredient(
    id = id,
    isBought = isBought,
    aisleCategory = "Veg $id",
    ingredientName = "Tomato $id",
    recipeId = recipeId,
    quantity = "4",
    recipeName = "Pasta $id",
    recipeImg = RecipeImage(
      recipeId,
      "jpg",
      RecipeImageSize.SMALL,
      ImageType.CELL
    ),
    servings = 2
  )
}

