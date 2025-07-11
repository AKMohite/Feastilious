// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentRecipeItemsBinding
import com.ak.feastit.databinding.ComponentTextItemBinding
import com.ak.feastit.ui.explore.experimental.ExploreItemAction
import com.ak.feastit.ui.yumdetail.tabs.overview.component.OverviewTextViewHolder
import com.ak.feastit.ui.yumdetail.tabs.overview.component.RecipeOverviewItem
import com.ak.feastit.ui.yumdetail.tabs.overview.component.SimilarRecipesViewHolder

internal class RecipeOverViewAdapter(
  private val sectionEvents: ((ExploreItemAction) -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
  AsyncListDiffer.ListListener<RecipeOverviewItem> {

  private val asyncDiff = AsyncListDiffer(this, RecipeOverviewDiff())
  private val headingType = 0
  private val textType = 1
  private val recipesType = 2

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      headingType -> {
        val binding =
          ComponentTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        OverviewTextViewHolder(binding, R.style.TextAppearance_FeastIt_TitleLarge)
      }

      textType -> {
        val binding =
          ComponentTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        OverviewTextViewHolder(binding, R.style.TextAppearance_FeastIt_BodyMedium)
      }

      recipesType -> {
        val binding =
          ComponentRecipeItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        SimilarRecipesViewHolder(binding, sectionEvents)
      }

      else -> {
        throw IllegalArgumentException("Invalid view type $viewType")
      }
    }
  }

  override fun getItemCount(): Int = asyncDiff.currentList.size

  override fun getItemViewType(position: Int): Int {
    return when (asyncDiff.currentList[position]) {
      is RecipeOverviewItem.Heading -> headingType
      is RecipeOverviewItem.Text -> textType
      is RecipeOverviewItem.Recipes -> recipesType
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val section = asyncDiff.currentList[position]
    when (holder.itemViewType) {
      headingType -> {
        val adapterItem = section as RecipeOverviewItem.Heading
        (holder as OverviewTextViewHolder).bind(adapterItem.text)
      }

      textType -> {
        val adapterItem = section as RecipeOverviewItem.Text
        (holder as OverviewTextViewHolder).bind(adapterItem.value)
      }

      recipesType -> {
        val adapterItem = section as RecipeOverviewItem.Recipes
        (holder as SimilarRecipesViewHolder).bind(adapterItem.items)
      }
    }
  }

  fun reload(items: List<RecipeOverviewItem>) {
    with(asyncDiff) {
      removeListListener(this@RecipeOverViewAdapter)
      addListListener(this@RecipeOverViewAdapter)
      submitList(items)
    }
  }

  override fun onCurrentListChanged(
    previousList: MutableList<RecipeOverviewItem>,
    currentList: MutableList<RecipeOverviewItem>,
  ) {
  }
}
