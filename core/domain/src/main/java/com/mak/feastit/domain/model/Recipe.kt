package com.mak.feastit.domain.model

data class Recipe (
    val id: Long,
    val recipeName: String,
    val recipeImgUrl: String,
    override val page: Int
): PaginatedEntry {
    fun isSameAs(newItem: Recipe): Boolean {
        return this.id == newItem.id &&
                this.recipeName == newItem.recipeName
    }
}