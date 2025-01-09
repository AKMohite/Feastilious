package com.mak.feastit.domain.model

data class Ingredient(
        val id: String,
        val image: String,
        val localizedName: String,
        val name: String,
        val quantity: String,
        val isBought: Boolean = false,
        val isInCart: Boolean = false
) {
    fun isSameAs(item: Ingredient): Boolean {
        return this.id == item.id &&
                this.isInCart == item.isInCart &&
                this.isBought == item.isBought
    }
}