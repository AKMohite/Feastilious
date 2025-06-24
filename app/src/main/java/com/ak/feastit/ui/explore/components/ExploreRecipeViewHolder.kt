package com.ak.feastit.ui.explore.components

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.ui.explore.experimental.ExploreItemAction
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.Recipe

internal class ExploreRecipeViewHolder(
    private val binding: ExploreRecipeItemBinding,
    private val sectionEvents: ((ExploreItemAction) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.root.onClick { sectionEvents?.invoke(ExploreItemAction.RecipeClick(recipe.id)) }
        binding.apply {
            recipeImg.load(recipe.image) {
                placeholder(R.drawable.ic_recipe_img_placeholder)
                error(R.drawable.ic_recipe_img_placeholder)
            }
            recipeName.text = recipe.name
        }
    }

}
