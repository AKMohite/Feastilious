package com.ak.feastit.ui.explore.experimental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionBinding
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem

internal class ExploreSectionAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sections: List<ExploreAdapterItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TOP_BANNER -> {
                val binding = ExploreRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopBannerViewHolder(binding)
            }
            HORIZONTAL_CHIPS -> {
                val binding = ComponentExploreSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionChipsViewHolder(binding)
            }
            HORIZONTAL_RECIPES -> {
                val binding = ComponentExploreSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionRecipesViewHolder(binding)
            }
            else -> throw IllegalStateException("Invalid view type $viewType rendering")
        }
    }

    override fun getItemCount(): Int = sections.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val adapterItem = sections[position]) {
            is ExploreAdapterItem.TopBanner -> (holder as TopBannerViewHolder).bind(adapterItem)
            is ExploreAdapterItem.HorizontalChips -> (holder as SectionChipsViewHolder).bind(adapterItem)
            is ExploreAdapterItem.HorizontalRecipes -> (holder as SectionRecipesViewHolder).bind(adapterItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(sections[position]) {
            is ExploreAdapterItem.TopBanner -> TOP_BANNER
            is ExploreAdapterItem.HorizontalChips -> HORIZONTAL_CHIPS
            is ExploreAdapterItem.HorizontalRecipes -> HORIZONTAL_RECIPES
        }
        return super.getItemViewType(position)
    }

    fun submitList(sections: List<ExploreAdapterItem>) {
        this.sections = sections
        notifyDataSetChanged()
    }

    companion object {
        const val TOP_BANNER = 1
        const val HORIZONTAL_CHIPS = 3
        const val HORIZONTAL_RECIPES = 4
    }
}