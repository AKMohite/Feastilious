// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore.experimental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.FilterTypeTextBinding
import com.ak.feastit.ui.explore.ExploreChip

internal class FilterChipsAdapter(
  private val items: List<ExploreChip>,
  private val sectionEvents: ((ExploreItemAction) -> Unit)? = null,
) : RecyclerView.Adapter<FilterViewHolder>() {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): FilterViewHolder {
    val binding = FilterTypeTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return FilterViewHolder(binding, sectionEvents)
  }

  override fun onBindViewHolder(
    holder: FilterViewHolder,
    position: Int,
  ) {
    holder.bind(items[position])
//        holder.itemView.onClick { sectionEvents(ExploreItemAction.ChipClick(items[position])) }
  }

  override fun getItemCount(): Int = items.size
}
