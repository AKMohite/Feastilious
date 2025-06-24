package com.ak.feastit.ui.viewall.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.databinding.ComponentViewAllRecipeBinding
import com.ak.feastit.utils.onClick
import com.ak.feastit.utils.show
import com.mak.feastit.domain.model.Recipe

class ComponentViewAllRecipe(
    private val binding: ComponentViewAllRecipeBinding,
    private val onRecipeClick: (Long) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.recipeImg.load(recipe.image)
        binding.recipeName.text = recipe.name
        binding.recipeName.show()
        binding.root.onClick { onRecipeClick(recipe.id) }
    }

}
