package com.ak.feastit.ui.explore.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ak.feastit.databinding.ComponentExploreSectionBinding
import com.ak.feastit.ui.recipes.ExploreSection

internal class SectionViewHolder(
    private val binding: ComponentExploreSectionBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(section: ExploreSection) {
        binding.sectionName.text = section.category.getTitle()
        binding.sectionSeeAllItems.visibility = if (section.hasMoreItems) View.VISIBLE else View.INVISIBLE
//        binding.sectionSeeAllItems.onClick {}
    }

}