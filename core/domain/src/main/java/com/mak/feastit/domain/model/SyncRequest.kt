package com.mak.feastit.domain.model

data class SyncRequest(
    val id: Int,
    val type: SyncType,
    val entityId: String? = null,
    val lastSyncedAt: Long
)

enum class SyncType {
    POPULAR_RECIPES,
    TOP_RATED_RECIPES,
    HEALTHY_RECIPES,
    QUICK_RECIPES,
    POCKET_FRIENDLY_RECIPES,
    SIMILAR_RECIPES,
    RECIPE_DETAILS,
    RECIPE_DETAIL_ANALYZED_INSTRUCTIONS
}
