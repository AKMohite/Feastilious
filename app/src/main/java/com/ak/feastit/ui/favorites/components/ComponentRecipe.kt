package com.ak.feastit.ui.favorites.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.databinding.ComponentRecipeBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.Recipe

class ComponentRecipe(
    private val binding: ComponentRecipeBinding,
    private val onRecipeClick: (Long) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.recipeImg.load(recipe.recipeImgUrl)
        binding.recipeName.text = recipe.recipeName
        binding.root.onClick { onRecipeClick(recipe.id) }
    }

}
