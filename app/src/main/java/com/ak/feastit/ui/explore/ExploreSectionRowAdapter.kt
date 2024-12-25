package com.ak.feastit.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.databinding.FilterTypeTextBinding
import com.ak.feastit.ui.explore.components.ExploreRecipeViewHolder
import com.ak.feastit.ui.explore.components.FilterTypeViewHolder
import com.ak.feastit.utils.onClick

internal class ExploreSectionRowAdapter(
    private val row: ExploreRow<*>,
    private val eventListener: SectionEventListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(row) {
            is ExploreRow.Chips -> {
                val binding = FilterTypeTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FilterTypeViewHolder(binding)
            }
            is ExploreRow.RecipeRows -> {
                val binding = ExploreRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExploreRecipeViewHolder(binding, null)
            }
        }
    }

    override fun getItemCount(): Int = row.contents.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(row) {
            is ExploreRow.Chips -> {
                val viewHolder = holder as FilterTypeViewHolder
                val chip = row.contents[position]
                viewHolder.itemView.onClick { eventListener?.onChipClick(chip) }
                viewHolder.bind(row.contents[position])
            }
            is ExploreRow.RecipeRows -> {
                val viewHolder = holder as ExploreRecipeViewHolder
                val recipe = row.contents[position]
                viewHolder.itemView.onClick { eventListener?.onRecipeClick(recipe.id) }
                viewHolder.bind(row.contents[position])
            }
        }
    }

}
