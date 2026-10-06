// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.ingredients.component

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.IngredientSection

internal class IngredientDiffUtil : DiffUtil.ItemCallback<IngredientSection>() {
  override fun areItemsTheSame(
    oldItem: IngredientSection,
    newItem: IngredientSection,
  ): Boolean = oldItem.areItemsTheSame(newItem)

  override fun areContentsTheSame(
    oldItem: IngredientSection,
    newItem: IngredientSection,
  ): Boolean = oldItem.areContentsTheSame(newItem)
}
