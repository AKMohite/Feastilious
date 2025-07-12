// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

import androidx.annotation.StringRes

sealed interface FavoriteWidgetType {
  val id: Long
  data class Item(
    val recipe: Recipe,
    override val id: Long = recipe.id,
  ) : FavoriteWidgetType
  data class Header(
    @StringRes val title: Int,
    override val id: Long = 1L,
  ) : FavoriteWidgetType
}
