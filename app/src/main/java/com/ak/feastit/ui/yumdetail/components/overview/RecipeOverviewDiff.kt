package com.ak.feastit.ui.yumdetail.components.overview

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.components.overview.component.RecipeOverviewItem

internal class RecipeOverviewDiff: DiffUtil.ItemCallback<RecipeOverviewItem>() {
    override fun areItemsTheSame(
        oldItem: RecipeOverviewItem,
        newItem: RecipeOverviewItem
    ): Boolean {
        return oldItem.areItemsSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: RecipeOverviewItem,
        newItem: RecipeOverviewItem
    ): Boolean {
        return oldItem.areContentsSame(newItem)
    }

}
