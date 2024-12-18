package com.ak.feastit.ui.explore

import com.mak.feastit.domain.model.SyncType

internal enum class ExploreCategory {
    BANNER_RECIPES,
    POPULAR_RECIPES,
    MEAL_TYPE_CHIPS,
    TOP_RATED_RECIPES,
    CUISINE_TYPE_CHIPS,
    HEALTHY_RECIPES,
    DIET_TYPE_CHIPS,
    QUICK_RECIPES,
    POCKET_FRIENDLY_RECIPES;

    fun toSyncType(): SyncType {
        return when(this) {
            POPULAR_RECIPES -> SyncType.POPULAR_RECIPES
            TOP_RATED_RECIPES -> SyncType.TOP_RATED_RECIPES
            HEALTHY_RECIPES -> SyncType.HEALTHY_RECIPES
            QUICK_RECIPES -> SyncType.QUICK_RECIPES
            POCKET_FRIENDLY_RECIPES -> SyncType.POCKET_FRIENDLY_RECIPES
            else -> throw IllegalArgumentException("Unknown sync type for $this")
        }
    }

    companion object {
        fun getRefreshExploreEntries(): List<ExploreCategory> {
            return entries.filterNot { category ->
                listOf(BANNER_RECIPES, MEAL_TYPE_CHIPS, CUISINE_TYPE_CHIPS, DIET_TYPE_CHIPS).contains(category)
            }
        }

        fun observerExploreEntries(): List<ExploreCategory> {
            return listOf(
                POPULAR_RECIPES,
                TOP_RATED_RECIPES,
                HEALTHY_RECIPES,
                QUICK_RECIPES,
                POCKET_FRIENDLY_RECIPES
            )
        }
    }
}