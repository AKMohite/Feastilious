package com.ak.feastit.ui.explore.components

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.FilterTypeTextBinding
import com.ak.feastit.ui.explore.ExploreChip

internal class FilterTypeViewHolder(
    private val binding: FilterTypeTextBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(exploreChip: ExploreChip) {
        binding.filterText.text = exploreChip.title
    }

}
