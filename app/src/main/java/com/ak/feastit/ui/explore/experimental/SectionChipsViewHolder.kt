package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionChipsBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem
import com.ak.feastit.utils.getEnumTitle

/**
 * This viewholder holds horizontal chips list and need to render all list items in recycler view
 */
internal class SectionChipsViewHolder(
    private val binding: ComponentExploreSectionChipsBinding,
    private val sectionEvents: ((ExploreItemAction) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root), NestedRecyclerViewViewHolder {

    override val layoutManager: RecyclerView.LayoutManager?
        get() = binding.sectionFilterItems.layoutManager

    fun bind(adapterItem: ExploreAdapterItem.HorizontalChips) {
        binding.sectionFilterName.text = adapterItem.category.name.getEnumTitle()
        binding.sectionFilterItems.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.sectionFilterItems.adapter = FilterChipsAdapter(adapterItem.items, sectionEvents)
    }

}
