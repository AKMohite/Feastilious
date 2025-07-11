// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class Recipe(
  val id: Long,
  val name: String,
  val image: String,
  override val page: Int,
) : PaginatedEntry {
  fun isSameAs(newItem: Recipe): Boolean = this.id == newItem.id &&
    this.name == newItem.name
}
