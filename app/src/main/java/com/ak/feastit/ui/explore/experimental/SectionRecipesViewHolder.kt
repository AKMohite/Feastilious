package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentExploreSectionChipsBinding
import com.ak.feastit.databinding.ComponentExploreSectionRecipeBinding
import com.ak.feastit.ui.explore.ExploreAdapterItem
import com.ak.feastit.utils.getEnumTitle

/**
 * This viewholder holds horizontal recipes list and need to render all list items in recycler view
 */
internal class SectionRecipesViewHolder(
    private val binding: ComponentExploreSectionRecipeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adapterItem: ExploreAdapterItem.HorizontalRecipes) {
        binding.sectionName.text = adapterItem.category.name.getEnumTitle()
        binding.sectionItems.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.sectionItems.adapter = ExploreRecipeAdapter(adapterItem.items)
    }

}
