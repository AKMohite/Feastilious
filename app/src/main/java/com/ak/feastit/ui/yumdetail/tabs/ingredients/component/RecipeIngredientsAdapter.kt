// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.ingredients.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentHeaderButtonBinding
import com.ak.feastit.databinding.ComponentRecipeIngredientBinding
import com.ak.feastit.ui.yumdetail.IngredientSection

private const val INGREDIENT_HEADER = 0
private const val INGREDIENT_ITEM = 1

internal class RecipeIngredientsAdapter(
  private val onToggleIngredientToCart: (String) -> Unit,
  private val onToggleAddToCart: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val asyncDiffUtil = AsyncListDiffer(this, IngredientDiffUtil())

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): RecyclerView.ViewHolder {
    return when (viewType) {
      INGREDIENT_HEADER -> {
        val binding =
          ComponentHeaderButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ComponentHeaderButton(binding, onToggleAddToCart)
      }

      INGREDIENT_ITEM -> {
        val binding = ComponentRecipeIngredientBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false,
        )
        return IngredientViewHolder(binding, onToggleIngredientToCart)
      }

      else -> throw IllegalStateException("Invalid view type $viewType rendering")
    }
  }

  override fun getItemCount(): Int = asyncDiffUtil.currentList.size

  override fun onBindViewHolder(
    holder: RecyclerView.ViewHolder,
    position: Int,
  ) {
    val item = asyncDiffUtil.currentList[position]
    when (holder.itemViewType) {
      INGREDIENT_HEADER -> {
        val section = item as IngredientSection.Header
        (holder as ComponentHeaderButton).bind(section.title)
      }

      INGREDIENT_ITEM -> {
        val section = item as IngredientSection.Item
        (holder as IngredientViewHolder).bind(section.ingredient)
      }
    }
  }

  override fun getItemViewType(position: Int): Int = when (asyncDiffUtil.currentList[position]) {
    is IngredientSection.Header -> INGREDIENT_HEADER
    is IngredientSection.Item -> INGREDIENT_ITEM
  }

  fun reload(list: List<IngredientSection>) {
    with(asyncDiffUtil) {
      submitList(list)
    }
  }
}
