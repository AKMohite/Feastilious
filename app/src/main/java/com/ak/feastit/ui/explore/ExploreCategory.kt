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

    fun isStaticCategory(): Boolean {
        return staticCategoryElements().contains(this)
    }

    companion object {

        fun fromName(name: String): ExploreCategory? {
            return ExploreCategory.entries.firstOrNull { it.name == name }
        }

        /**
         * These categories have to synced
         */
        fun getRefreshExploreEntries(): List<ExploreCategory> {
            return entries.filterNot { category ->
                staticCategoryElements().contains(category)
            }
        }

        /**
         * These categories are static and do not need to be synced
         */
        fun staticCategoryElements() =
            listOf(BANNER_RECIPES, MEAL_TYPE_CHIPS, CUISINE_TYPE_CHIPS, DIET_TYPE_CHIPS)

        /**
         * These categories are observed for changes from api sync
         */
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