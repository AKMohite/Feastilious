package com.ak.feastit.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionBinding
import com.ak.feastit.ui.explore.components.SectionViewHolder
import com.ak.feastit.ui.recipes.ExploreSection

internal class ExploreSectionAdapter: RecyclerView.Adapter<SectionViewHolder>() {

    private var sections: List<ExploreSection> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ComponentExploreSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun getItemCount(): Int = sections.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.bind(section)
    }

    fun submitList(sections: List<ExploreSection>) {
        this.sections = sections
        notifyDataSetChanged()
    }

}
