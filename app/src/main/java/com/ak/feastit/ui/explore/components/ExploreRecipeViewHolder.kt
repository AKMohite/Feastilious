package com.ak.feastit.ui.explore.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.mak.feastit.domain.model.Recipe

internal class ExploreRecipeViewHolder(
    private val binding: ExploreRecipeItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.apply {
            recipeImg.load(recipe.recipeImgUrl) {
                placeholder(R.drawable.ic_recipe_img_placeholder)
                error(R.drawable.ic_recipe_img_placeholder)
            }
            recipeName.text = recipe.recipeName
        }
    }

}
