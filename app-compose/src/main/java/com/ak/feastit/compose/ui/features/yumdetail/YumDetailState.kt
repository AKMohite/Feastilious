// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.yumdetail

import androidx.annotation.StringRes
import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Nutrient
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail

internal data class YumDetailState(
  val overview: RecipeDetail? = null,
  val similarRecipes: List<Recipe> = emptyList(),
  val instructions: List<StepSection> = emptyList(),
  val ingredientSections: List<IngredientSection> = emptyList(),
  val nutrients: List<Nutrient> = emptyList(),
) {
  val areIngredientsInCart =
    ingredientSections
      .filterIsInstance<IngredientSection.Item>()
      .any { section -> section.ingredient.isInCart }
}

internal sealed interface IngredientSection {
  fun areItemsTheSame(newItem: IngredientSection): Boolean

  fun areContentsTheSame(newItem: IngredientSection): Boolean

  data class Header(
    @StringRes val title: Int,
  ) : IngredientSection {
    override fun areItemsTheSame(newItem: IngredientSection): Boolean = newItem is Header

    override fun areContentsTheSame(newItem: IngredientSection): Boolean {
      val section = newItem as? Header ?: return false
      return title == section.title
    }
  }

  data class Item(
    val ingredient: Ingredient,
  ) : IngredientSection {
    override fun areItemsTheSame(newItem: IngredientSection): Boolean = newItem is Item && ingredient.id == newItem.ingredient.id

    override fun areContentsTheSame(newItem: IngredientSection): Boolean {
      val item = (newItem as? Item)?.ingredient ?: return false
      return ingredient.isSameAs(item)
    }
  }
}

internal sealed interface StepSection {
  fun areItemsTheSame(newItem: StepSection): Boolean

  fun areContentsTheSame(newItem: StepSection): Boolean

  data class Header(
    @StringRes val title: Int,
  ) : StepSection {
    override fun areItemsTheSame(newItem: StepSection): Boolean = newItem is Header

    override fun areContentsTheSame(newItem: StepSection): Boolean {
      val section = newItem as? Header ?: return false
      return title == section.title
    }
  }

  data class Item(
    val instruction: Instruction,
  ) : StepSection {
    override fun areItemsTheSame(newItem: StepSection): Boolean = newItem is Item

    override fun areContentsTheSame(newItem: StepSection): Boolean {
      val item = (newItem as? Item)?.instruction ?: return false
      return instruction.isSameAs(item)
    }
  }
}
