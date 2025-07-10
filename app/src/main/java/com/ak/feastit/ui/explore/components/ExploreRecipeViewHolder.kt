package com.ak.feastit.ui.explore.components

import android.view.View
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
        val image = binding.root.context.getString(R.string.recipe_to_detail_image, recipe.id)
        val name = binding.root.context.getString(R.string.recipe_to_detail_name, recipe.id)
        val sharedElements: Map<View, String> = mapOf(
            binding.recipeImg to image,
            binding.recipeName to name
        )
        binding.root.onClick {
            sectionEvents?.invoke(
                ExploreItemAction.RecipeClick(
                    sharedElements,
                    recipe.id
                )
            )
        }
        binding.apply {
            recipeImg.load(recipe.image) {
                placeholder(R.drawable.ic_recipe_img_placeholder)
                error(R.drawable.ic_recipe_img_placeholder)
            }
            recipeImg.transitionName = image
            recipeName.transitionName = name
            recipeName.text = recipe.name
        }
    }

}
