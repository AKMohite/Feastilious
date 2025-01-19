package com.ak.feastit.ui.mealplanner.tabs.unscheduled.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentMealPlanRecipeBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.MealPlanRecipe

class ComponentMealPlanRecipe(
    private val binding: ComponentMealPlanRecipeBinding,
    private val onMenuClick: ((recipe: MealPlanRecipe) -> Unit)? = null
): RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: MealPlanRecipe) {
        binding.recipeImg.load(recipe.image)
        binding.recipeName.text = recipe.name
        binding.moreMenu.onClick { onMenuClick?.invoke(recipe) }
        val (isPreparation, time) = recipe.preparationOrSchedule()
        val icon = if (isPreparation) {
            R.drawable.ic_time_required
        } else {
            R.drawable.ic_schduled_at
        }
        binding.preparationTime.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        binding.preparationTime.text = time
    }
}