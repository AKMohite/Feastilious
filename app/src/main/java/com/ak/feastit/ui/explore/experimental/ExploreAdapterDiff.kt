package com.ak.feastit.ui.explore.experimental

import com.ak.feastit.ui.explore.ExploreAdapterItem

internal class ExploreAdapterDiff: androidx.recyclerview.widget.DiffUtil.ItemCallback<ExploreAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: ExploreAdapterItem,
        newItem: ExploreAdapterItem
    ): Boolean {
        return oldItem.category.ordinal == newItem.category.ordinal
    }

    override fun areContentsTheSame(
        oldItem: ExploreAdapterItem,
        newItem: ExploreAdapterItem
    ): Boolean {
        return oldItem.category.ordinal == newItem.category.ordinal && oldItem.category == newItem.category
    }

}