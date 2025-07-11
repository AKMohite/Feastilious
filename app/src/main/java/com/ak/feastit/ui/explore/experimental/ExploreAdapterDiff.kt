// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore.experimental

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.explore.ExploreAdapterItem

internal class ExploreAdapterDiff : DiffUtil.ItemCallback<ExploreAdapterItem>() {
  override fun areItemsTheSame(
    oldItem: ExploreAdapterItem,
    newItem: ExploreAdapterItem,
  ): Boolean = oldItem.category.ordinal == newItem.category.ordinal

  override fun areContentsTheSame(
    oldItem: ExploreAdapterItem,
    newItem: ExploreAdapterItem,
  ): Boolean = oldItem.isSameAs(newItem)
}
