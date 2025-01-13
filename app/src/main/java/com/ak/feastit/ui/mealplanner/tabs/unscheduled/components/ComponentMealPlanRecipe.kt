package com.ak.feastit.ui.mealplanner.tabs.unscheduled.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.databinding.ComponentMealPlanRecipeBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.MealPlanRecipe

class ComponentMealPlanRecipe(
    private val binding: ComponentMealPlanRecipeBinding,
    private val onMenuClick: (recipeId: Long) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: MealPlanRecipe) {
        binding.recipeImg.load(recipe.image)
        binding.recipeName.text = recipe.name
        binding.moreMenu.onClick { onMenuClick(recipe.recipeId) }
    }
}