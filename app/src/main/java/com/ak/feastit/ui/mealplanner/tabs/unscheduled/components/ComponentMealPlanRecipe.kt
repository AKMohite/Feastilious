// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.tabs.unscheduled.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentMealPlanRecipeBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.MealPlanRecipe

internal class ComponentMealPlanRecipe(
  private val binding: ComponentMealPlanRecipeBinding,
  private val onMealPlanClick: ((OnMealPlanClick) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(recipe: MealPlanRecipe) {
    binding.root.onClick { onMealPlanClick?.invoke(OnMealPlanClick.RecipeDetail(recipe)) }
    binding.recipeImg.load(recipe.image)
    binding.recipeName.text = recipe.name
    binding.moreMenu.onClick { onMealPlanClick?.invoke(OnMealPlanClick.MoreMenu(recipe)) }
    val (isPreparation, time) = recipe.preparationOrSchedule()
    val icon =
      if (isPreparation) {
        R.drawable.ic_time_required
      } else {
        R.drawable.ic_schduled_at
      }
    binding.preparationTime.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    binding.preparationTime.text = time
  }
}

internal sealed interface OnMealPlanClick {
  data class MoreMenu(
    val recipe: MealPlanRecipe,
  ) : OnMealPlanClick

  data class RecipeDetail(
    val recipe: MealPlanRecipe,
  ) : OnMealPlanClick
}
