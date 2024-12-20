package com.ak.feastit.ui.explore.experimental

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponetExploreHeaderBannerBinding
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem

/**
 * This viewholder has top banner items as list that need to render all list items in pager
 */
internal class TopBannerViewHolder(
    private val binding: ComponetExploreHeaderBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adapterItem: ExploreAdapterItem.TopBanner, fragmentManager: FragmentManager, lifecycle: Lifecycle) {
        binding.headerPager.adapter = ExploreHeaderPagerAdapter(fragmentManager, lifecycle, adapterItem.items)
    }
}
