package com.ak.feastit.ui.cart

import com.mak.feastit.domain.model.CartIngredient

internal data class CartState(
    val cart: List<ShoppingCart> = emptyList()
)

internal data class CartRecipe(
    val id: Long,
    val name: String,
    val img: String,
    val servings: Int
) {
    fun isSameAs(recipe: CartRecipe): Boolean {
        return this.id == recipe.id &&
                recipe.name == this.name
    }
}

internal sealed interface ShoppingCart {

    fun areItemsTheSame(other: ShoppingCart): Boolean
    fun areContentsTheSame(other: ShoppingCart): Boolean

    data class AisleCategory(val title: String) : ShoppingCart {
        override fun areItemsTheSame(other: ShoppingCart): Boolean {
            return other is AisleCategory && other == this@AisleCategory
        }

        override fun areContentsTheSame(other: ShoppingCart): Boolean {
            val category = other as? AisleCategory ?: return false
            return category.title == this.title
        }
    }

    data class RecipeHeading(val recipe: CartRecipe): ShoppingCart {
        override fun areItemsTheSame(other: ShoppingCart): Boolean {
            return other is RecipeHeading && other == this@RecipeHeading
        }

        override fun areContentsTheSame(other: ShoppingCart): Boolean {
            val recipe = (other as? RecipeHeading)?.recipe ?: return false
            return recipe.isSameAs(this.recipe)
        }
    }

    data class Ingredient(val ingredient: CartIngredient): ShoppingCart {
        override fun areItemsTheSame(other: ShoppingCart): Boolean {
            return other is Ingredient && other == this@Ingredient
        }

        override fun areContentsTheSame(other: ShoppingCart): Boolean {
            val ingredient = (other as? Ingredient)?.ingredient ?: return false
            return ingredient.isSameAs(ingredient)
        }
    }
}
