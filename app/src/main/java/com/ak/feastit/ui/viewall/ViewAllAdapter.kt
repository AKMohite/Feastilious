// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.viewall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.databinding.ComponentViewAllRecipeBinding
import com.ak.feastit.ui.viewall.components.ComponentViewAllRecipe
import com.mak.feastit.domain.model.Recipe

internal class ViewAllAdapter(
  private val onRecipeClick: (sharedElements: Map<View, String>, recipeId: Long) -> Unit,
) : PagingDataAdapter<Recipe, ComponentViewAllRecipe>(
  ViewAllDiff,
) {
  override fun onBindViewHolder(
    holder: ComponentViewAllRecipe,
    position: Int,
  ) {
    val recipe = getItem(position) ?: return
    holder.bind(recipe)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): ComponentViewAllRecipe {
    val binding =
      ComponentViewAllRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ComponentViewAllRecipe(binding, onRecipeClick = onRecipeClick)
  }
}

internal object ViewAllDiff : DiffUtil.ItemCallback<Recipe>() {
  override fun areItemsTheSame(
    oldItem: Recipe,
    newItem: Recipe,
  ): Boolean = oldItem == newItem

  override fun areContentsTheSame(
    oldItem: Recipe,
    newItem: Recipe,
  ): Boolean = oldItem.isSameAs(newItem)
}
