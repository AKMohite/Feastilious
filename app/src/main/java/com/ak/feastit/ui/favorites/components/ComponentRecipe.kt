package com.ak.feastit.ui.favorites.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentRecipeBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.Recipe

class ComponentRecipe(
    private val binding: ComponentRecipeBinding,
    private val onRecipeClick: (sharedElements: Map<View, String>, recipeId: Long) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        val image = binding.root.context.getString(R.string.recipe_to_detail_image, recipe.id)
        val name = binding.root.context.getString(R.string.recipe_to_detail_name, recipe.id)
        val sharedElements: Map<View, String> = mapOf(
            binding.recipeImg to image,
            binding.recipeName to name
        )
        binding.recipeImg.transitionName = image
        binding.recipeName.transitionName = name
        binding.recipeImg.load(recipe.image)
        binding.recipeName.text = recipe.name
        binding.root.onClick { onRecipeClick(sharedElements, recipe.id) }
    }

}
