package com.ak.feastit.ui.mealplanner.tabs.unscheduled.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentMealPlanRecipeBinding
import com.mak.feastit.domain.model.MealPlanRecipe

internal class UnscheduledRecipeAdapter(
    private val onMenuClick: (recipe: MealPlanRecipe) -> Unit
): RecyclerView.Adapter<ComponentMealPlanRecipe>() {

    private val asyncDiff = AsyncListDiffer(this, UnscheduledRecipeDiff())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentMealPlanRecipe {
        val binding = ComponentMealPlanRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComponentMealPlanRecipe(binding, onMenuClick)
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun onBindViewHolder(holder: ComponentMealPlanRecipe, position: Int) {
        val recipe = asyncDiff.currentList[position]
        holder.bind(recipe)
    }

    fun reload(items: List<MealPlanRecipe>) {
        asyncDiff.submitList(items)
    }
}