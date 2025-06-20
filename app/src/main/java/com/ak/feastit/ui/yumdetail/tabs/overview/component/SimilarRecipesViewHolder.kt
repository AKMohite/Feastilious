package com.ak.feastit.ui.yumdetail.tabs.overview.component

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeItemsBinding
import com.ak.feastit.ui.explore.experimental.ExploreItemAction
import com.ak.feastit.ui.explore.experimental.ExploreRecipeAdapter
import com.mak.feastit.domain.model.Recipe

internal class SimilarRecipesViewHolder(
    private val binding: ComponentRecipeItemsBinding,
    private val sectionEvents: ((ExploreItemAction) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(items: List<Recipe>) {
        binding.recipeList.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recipeList.adapter = ExploreRecipeAdapter(items, sectionEvents)
    }
}
