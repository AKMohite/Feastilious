package com.ak.feastit.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentViewAllRecipeBinding
import com.ak.feastit.ui.viewall.components.ComponentViewAllRecipe
import com.mak.feastit.domain.model.Recipe

internal class SearchResultAdapter(
    private val onRecipeClick: (Long) -> Unit
): RecyclerView.Adapter<ComponentViewAllRecipe>() {

    private val asyncDiff = AsyncListDiffer(this, SearchResultDiff)

    override fun onBindViewHolder(holder: ComponentViewAllRecipe, position: Int) {
        val recipe = asyncDiff.currentList[position]
        holder.bind(recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewAllRecipe {
        val binding = ComponentViewAllRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComponentViewAllRecipe(binding, onRecipeClick = onRecipeClick)
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    fun reload(recipes: List<Recipe>) {
        asyncDiff.submitList(recipes)
    }
}

internal object SearchResultDiff : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.isSameAs(newItem)
    }

}
