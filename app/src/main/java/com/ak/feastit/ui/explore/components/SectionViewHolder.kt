package com.ak.feastit.ui.explore.components

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionBinding
import com.ak.feastit.ui.explore.ExploreSection
import com.ak.feastit.ui.explore.ExploreSectionRowAdapter
import com.ak.feastit.ui.explore.SectionEventListener
import com.ak.feastit.utils.getEnumTitle
import com.ak.feastit.utils.onClick
import com.ak.feastit.utils.show

internal class SectionViewHolder(
    private val binding: ComponentExploreSectionBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(section: ExploreSection, eventListener: SectionEventListener?) {
        if (section.row == null) return
        binding.sectionName.text = section.category.name.getEnumTitle()
        binding.sectionSeeAllItems.show(section.hasMoreItems, false)
        binding.sectionItems.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.sectionItems.adapter = ExploreSectionRowAdapter(section.row, eventListener)

        binding.sectionSeeAllItems.onClick { eventListener?.viewAll(section.category) }
    }

}