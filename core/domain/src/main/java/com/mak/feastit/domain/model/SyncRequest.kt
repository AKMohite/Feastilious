// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class SyncRequest(
  val id: Int,
  val type: SyncType,
  val entityId: String? = null,
  val lastSyncedAt: Long,
)

/**
 * This class represents the local sync to handle api calls
 */
enum class SyncType {
  POPULAR_RECIPES,
  TOP_RATED_RECIPES,
  HEALTHY_RECIPES,
  QUICK_RECIPES,
  POCKET_FRIENDLY_RECIPES,
  RECIPE_WITH_SIMILAR_RECIPES, // recipe having same recipes
  RECIPE_DETAILS, // recipe with its details
  RECIPE_ANALYZED_INSTRUCTIONS, // recipe with steps
}
