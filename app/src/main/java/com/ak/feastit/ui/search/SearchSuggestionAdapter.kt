package com.ak.feastit.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeBinding
import com.ak.feastit.databinding.ComponentSearchHeaderItemBinding
import com.ak.feastit.databinding.ComponentSearchSuggestionTextBinding
import com.ak.feastit.ui.favorites.components.ComponentRecipe
import com.ak.feastit.ui.search.components.ComponentSearchHeader
import com.ak.feastit.ui.search.components.ComponentSearchRecentSuggestion

internal class SearchSuggestionAdapter(
    private val onRecipeClick: (Long) -> Unit,
    private val onHistoryClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncDiff = AsyncListDiffer(this, SearchSuggestionDiff())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            HEADER -> {
                val binding = ComponentSearchHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ComponentSearchHeader(binding)
            }

            HISTORY -> {
                val binding = ComponentSearchSuggestionTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ComponentSearchRecentSuggestion(binding, onHistoryClick)
            }

            RECOMMENDED_RECIPE -> {
                val binding = ComponentRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ComponentRecipe(binding, onRecipeClick)
            }

            else -> throw IllegalStateException("Invalid view type $viewType rendering")
        }
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = asyncDiff.currentList[position]
        when (holder.itemViewType) {
            HEADER -> {
                val adapterItem = item as SearchSuggestionItem.Header
                (holder as ComponentSearchHeader).bind(adapterItem)
            }

            HISTORY -> {
                val adapterItem = item as SearchSuggestionItem.History
                (holder as ComponentSearchRecentSuggestion).bind(adapterItem)
            }

            RECOMMENDED_RECIPE -> {
                val adapterItem = item as SearchSuggestionItem.Recommendation
                (holder as ComponentRecipe).bind(adapterItem.recipe)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (asyncDiff.currentList[position]) {
            is SearchSuggestionItem.Header -> HEADER
            is SearchSuggestionItem.History -> HISTORY
            is SearchSuggestionItem.Recommendation -> RECOMMENDED_RECIPE
        }
    }

    fun submitList(suggestions: List<SearchSuggestionItem>) {
        asyncDiff.submitList(suggestions)
    }

    fun getType(position: Int): SearchSuggestionItem? {
        return asyncDiff.currentList[position]
    }


    companion object {
        private const val HEADER = 0
        private const val HISTORY = 1
        private const val RECOMMENDED_RECIPE = 2
    }

}