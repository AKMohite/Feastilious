package com.ak.feastit.ui.favorites.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeBinding
import com.mak.feastit.domain.model.Recipe

internal class FavoritesAdapter(
    private val onRecipeClick: (sharedElements: Map<View, String>, recipeId: Long) -> Unit
): RecyclerView.Adapter<ComponentRecipe>() {

    private val asyncDiff = AsyncListDiffer(this, RecipeDiff())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentRecipe {
        val binding = ComponentRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComponentRecipe(binding, onRecipeClick)
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun onBindViewHolder(holder: ComponentRecipe, position: Int) {
        val recipe = asyncDiff.currentList[position]
        holder.bind(recipe)
    }

    fun reload(recipes: List<Recipe>) {
        asyncDiff.submitList(recipes)
    }

}
