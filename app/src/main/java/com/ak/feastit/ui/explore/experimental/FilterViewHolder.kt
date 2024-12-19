package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.FilterTypeTextBinding
import com.ak.feastit.ui.explore.ExploreChip
import com.ak.feastit.utils.getEnumTitle

internal class FilterViewHolder(
    private val binding: FilterTypeTextBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ExploreChip) {
        binding.filterText.text = item.name.getEnumTitle()
    }
}
