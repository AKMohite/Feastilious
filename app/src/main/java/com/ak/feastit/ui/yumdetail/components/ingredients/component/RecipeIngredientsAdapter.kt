package com.ak.feastit.ui.yumdetail.components.ingredients.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeIngredientBinding
import com.mak.feastit.domain.model.Ingredient

internal class RecipeIngredientsAdapter : RecyclerView.Adapter<IngredientViewHolder>() {

    private val asyncDiffUtil = AsyncListDiffer(this, IngredientDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ComponentRecipeIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncDiffUtil.currentList.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = asyncDiffUtil.currentList[position]
        holder.bind(ingredient)
    }

    fun reload(list: List<Ingredient>) {
        with(asyncDiffUtil) {
            submitList(list)
        }
    }

}
