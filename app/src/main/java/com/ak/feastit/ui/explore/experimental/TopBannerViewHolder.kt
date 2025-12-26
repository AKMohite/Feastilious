// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore.experimental

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponetExploreHeaderBannerBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem

/**
 * This viewholder has top banner items as list that need to render all list items in pager
 */
internal class TopBannerViewHolder(
  private val binding: ComponetExploreHeaderBannerBinding,
  private val sectionEvents: ((ExploreItemAction) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root),
  NestedRecyclerViewViewHolder {
  /*override val layoutManager: RecyclerView.LayoutManager?
    get() =
      binding.headerPager.javaClass
        .getDeclaredField("mRecyclerView")
        .let {
          it.isAccessible = true
          (it.get(binding.headerPager) as? RecyclerView)?.layoutManager
        }*/

  override val layoutManager: RecyclerView.LayoutManager?
    get() {
      val viewPager = binding.headerPager
      val recyclerView = viewPager.getChildAt(0) as? RecyclerView
      return recyclerView?.layoutManager
    }

  fun bind(
    adapterItem: ExploreAdapterItem.TopBanner,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
  ) {
    binding.headerPager.adapter =
      ExploreHeaderPagerAdapter(
        fragmentManager = fragmentManager,
        lifecycle = lifecycle,
        items = adapterItem.items,
        sectionEvents = sectionEvents,
      )
  }
}
