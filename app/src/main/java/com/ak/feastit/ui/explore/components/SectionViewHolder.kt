package com.ak.feastit.ui.explore.components

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionBinding
import com.ak.feastit.ui.explore.ExploreSection
import com.ak.feastit.ui.explore.ExploreSectionRowAdapter

internal class SectionViewHolder(
    private val binding: ComponentExploreSectionBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(section: ExploreSection) {
        if (section.row == null) return
        binding.sectionName.text = section.category.getTitle()
        binding.sectionSeeAllItems.visibility = if (section.hasMoreItems) View.VISIBLE else View.INVISIBLE
        binding.sectionItems.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.sectionItems.adapter = ExploreSectionRowAdapter(section.row)

//        binding.sectionSeeAllItems.onClick {}
    }

}