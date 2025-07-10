package com.mak.feastit.domain.model

data class Recipe (
    val id: Long,
    val name: String,
    val image: String,
    override val page: Int
): PaginatedEntry {
    fun isSameAs(newItem: Recipe): Boolean {
        return this.id == newItem.id &&
                this.name == newItem.name
    }
}