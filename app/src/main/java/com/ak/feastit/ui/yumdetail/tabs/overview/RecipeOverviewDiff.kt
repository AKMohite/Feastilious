// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.overview

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.tabs.overview.component.RecipeOverviewItem

internal class RecipeOverviewDiff : DiffUtil.ItemCallback<RecipeOverviewItem>() {
  override fun areItemsTheSame(
    oldItem: RecipeOverviewItem,
    newItem: RecipeOverviewItem,
  ): Boolean = oldItem.areItemsSame(newItem)

  override fun areContentsTheSame(
    oldItem: RecipeOverviewItem,
    newItem: RecipeOverviewItem,
  ): Boolean = oldItem.areContentsSame(newItem)
}
