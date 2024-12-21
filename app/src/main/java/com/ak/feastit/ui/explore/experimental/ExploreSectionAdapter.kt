package com.ak.feastit.ui.explore.experimental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionChipsBinding
import com.ak.feastit.databinding.ComponentExploreSectionRecipeBinding
import com.ak.feastit.databinding.ComponetExploreHeaderBannerBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem
import com.ak.feastit.ui.explore.ExploreCategory
import com.ak.feastit.ui.explore.ExploreChip


/**
 * This adapter is for parent recycler view showing different views in it
 * for eg. top banner, horizontal chips and horizontal recipes
 * and each view has its own view holder containing child items to display in respective viewholder
 * and adapters
 */
internal class ExploreSectionAdapter(
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle,
    private val sectionEvents: ((ExploreItemAction) -> Unit)? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>(), AsyncListDiffer.ListListener<ExploreAdapterItem> {

    private val asyncDiff = AsyncListDiffer(this, ExploreAdapterDiff())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TOP_HORIZONTAL_BANNER -> {
                val binding = ComponetExploreHeaderBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                TODO handle list for banners
                TopBannerViewHolder(binding, sectionEvents)
            }
            HORIZONTAL_CHIPS -> {
                val binding = ComponentExploreSectionChipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionChipsViewHolder(binding, sectionEvents)
            }
            HORIZONTAL_RECIPES -> {
                val binding = ComponentExploreSectionRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionRecipesViewHolder(binding, sectionEvents)
            }
            else -> throw IllegalStateException("Invalid view type $viewType rendering")
        }
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = asyncDiff.currentList[position]
        when(holder.itemViewType) {
            TOP_HORIZONTAL_BANNER -> {
                val adapterItem = section as ExploreAdapterItem.TopBanner
                (holder as TopBannerViewHolder).bind(adapterItem, fragmentManager, lifecycle)
            }
            HORIZONTAL_CHIPS -> {
                val adapterItem = section as ExploreAdapterItem.HorizontalChips
                (holder as SectionChipsViewHolder).bind(adapterItem)
            }
            HORIZONTAL_RECIPES -> {
                val adapterItem = section as ExploreAdapterItem.HorizontalRecipes
                (holder as SectionRecipesViewHolder).bind(adapterItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(asyncDiff.currentList[position]) {
            is ExploreAdapterItem.TopBanner -> TOP_HORIZONTAL_BANNER
            is ExploreAdapterItem.HorizontalChips -> HORIZONTAL_CHIPS
            is ExploreAdapterItem.HorizontalRecipes -> HORIZONTAL_RECIPES
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ExploreAdapterItem>,
        currentList: MutableList<ExploreAdapterItem>
    ) {
        sectionEvents?.invoke(ExploreItemAction.ListUpdate)
    }

    fun submitList(sections: List<ExploreAdapterItem>) {
        with(asyncDiff) {
            removeListListener(this@ExploreSectionAdapter)
            addListListener(this@ExploreSectionAdapter)
            submitList(sections)
        }
    }

    companion object {
        const val TOP_HORIZONTAL_BANNER = 0
        const val HORIZONTAL_CHIPS = 1
        const val HORIZONTAL_RECIPES = 2
    }
}

internal sealed interface ExploreItemAction {
    data class ViewAll(val category: ExploreCategory): ExploreItemAction
    data class RecipeClick(val recipeId: Long): ExploreItemAction
    data class ChipClick(val chip: ExploreChip): ExploreItemAction
    data object ListUpdate: ExploreItemAction
}
