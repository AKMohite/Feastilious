package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.RecyclerView
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
//            recipeImg.setImageResource(adapterItem.items[0].recipeImgUrl)
            recipeName.text = adapterItem.items[0].recipeName
        }
    }
}
