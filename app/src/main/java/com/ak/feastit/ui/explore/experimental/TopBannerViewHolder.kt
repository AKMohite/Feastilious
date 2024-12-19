package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem

/**
 * This viewholder has top banner items as list that need to render all list items in pager
 */
internal class TopBannerViewHolder(
    private val binding: ExploreRecipeItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adapterItem: ExploreAdapterItem.TopBanner) {
        binding.apply {
            val recipe = adapterItem.items[0]
            recipeImg.load(recipe.recipeImgUrl) {
                placeholder(R.drawable.ic_recipe_img_placeholder)
                error(R.drawable.ic_recipe_img_placeholder)
            }
            recipeName.text = recipe.recipeName
        }
    }
}
