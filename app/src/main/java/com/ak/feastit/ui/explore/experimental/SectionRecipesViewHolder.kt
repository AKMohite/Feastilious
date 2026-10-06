// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionRecipeBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem
import com.ak.feastit.utils.getEnumTitle
import com.ak.feastit.utils.onClick

/**
 * This viewholder holds horizontal recipes list and need to render all list items in recycler view
 */
internal class SectionRecipesViewHolder(
  private val binding: ComponentExploreSectionRecipeBinding,
  private val sectionEvents: ((ExploreItemAction) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root),
  NestedRecyclerViewViewHolder {
  override val layoutManager: RecyclerView.LayoutManager?
    get() = binding.sectionItems.layoutManager

  fun bind(adapterItem: ExploreAdapterItem.HorizontalRecipes) {
    binding.sectionName.text = adapterItem.category.name.getEnumTitle()
    binding.sectionItems.layoutManager =
      LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
    binding.sectionItems.adapter = ExploreRecipeAdapter(adapterItem.items, sectionEvents)
    binding.sectionSeeAllItems.onClick { sectionEvents?.invoke(ExploreItemAction.ViewAll(adapterItem.category)) }
  }
}
