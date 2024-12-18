package com.ak.feastit.ui.explore.experimental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionChipsBinding
import com.ak.feastit.databinding.ComponentExploreSectionRecipeBinding
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem


/**
 * This adapter is for parent recycler view showing different views in it
 * for eg. top banner, horizontal chips and horizontal recipes
 * and each view has its own view holder containing child items to display in respective viewholder
 * and adapters
 */
internal class ExploreSectionAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sections: List<ExploreAdapterItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TOP_HORIZONTAL_BANNER -> {
                val binding = ExploreRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                TODO handle list for banners
                TopBannerViewHolder(binding)
            }
            HORIZONTAL_CHIPS -> {
                val binding = ComponentExploreSectionChipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionChipsViewHolder(binding)
            }
            HORIZONTAL_RECIPES -> {
                val binding = ComponentExploreSectionRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionRecipesViewHolder(binding)
            }
            else -> throw IllegalStateException("Invalid view type $viewType rendering")
        }
    }

    override fun getItemCount(): Int = sections.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            TOP_HORIZONTAL_BANNER -> {
                val adapterItem = sections[position] as ExploreAdapterItem.TopBanner
                (holder as TopBannerViewHolder).bind(adapterItem)
            }
            HORIZONTAL_CHIPS -> {
                val adapterItem = sections[position] as ExploreAdapterItem.HorizontalChips
                (holder as SectionChipsViewHolder).bind(adapterItem)
            }
            HORIZONTAL_RECIPES -> {
                val adapterItem = sections[position] as ExploreAdapterItem.HorizontalRecipes
                (holder as SectionRecipesViewHolder).bind(adapterItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(sections[position]) {
            is ExploreAdapterItem.TopBanner -> TOP_HORIZONTAL_BANNER
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
        const val TOP_HORIZONTAL_BANNER = 0
        const val HORIZONTAL_CHIPS = 1
        const val HORIZONTAL_RECIPES = 2
    }
}