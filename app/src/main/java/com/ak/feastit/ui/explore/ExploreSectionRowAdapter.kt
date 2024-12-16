package com.ak.feastit.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.databinding.FilterTypeTextBinding
import com.ak.feastit.ui.explore.components.ExploreRecipeViewHolder
import com.ak.feastit.ui.explore.components.FilterTypeViewHolder

internal class ExploreSectionRowAdapter(
    private val row: ExploreRow<*>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(row) {
            is ExploreRow.Chips -> {
                val binding = FilterTypeTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FilterTypeViewHolder(binding)
            }
            is ExploreRow.RecipeRows -> {
                val binding = ExploreRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExploreRecipeViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = row.contents.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(row) {
            is ExploreRow.Chips -> (holder as FilterTypeViewHolder).bind(row.contents[position])
            is ExploreRow.RecipeRows -> (holder as ExploreRecipeViewHolder).bind(row.contents[position])
        }
    }

}
