package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.FilterTypeTextBinding
import com.ak.feastit.ui.explore.ExploreChip
import com.ak.feastit.utils.getEnumTitle
import com.ak.feastit.utils.onClick

internal class FilterViewHolder(
    private val binding: FilterTypeTextBinding,
    private val sectionEvents: ((ExploreItemAction) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ExploreChip) {
        binding.filterText.text = item.name.getEnumTitle()
        binding.root.onClick {
            sectionEvents?.invoke(ExploreItemAction.ChipClick(item))
        }
    }
}
