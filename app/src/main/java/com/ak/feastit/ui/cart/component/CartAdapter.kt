package com.ak.feastit.ui.cart.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentCartHeadingAisleBinding
import com.ak.feastit.databinding.ComponentCartHeadingRecipeBinding
import com.ak.feastit.databinding.ComponentCartIngredientBinding
import com.ak.feastit.ui.cart.ShoppingCart

private const val COMPONENT_RECIPE = 0
private const val COMPONENT_AISLE = 1
private const val COMPONENT_INGREDIENT = 2

sealed interface CartEvent {
    data class RecipeDetail(val id: Long): CartEvent
    data class ToggleIngredient(val ingredientId: String) : CartEvent
    data class RemoveRecipe(val recipeId: Long): CartEvent
}

internal class CartAdapter(
    private val onCartEvent: (CartEvent) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncDiff = AsyncListDiffer(this, CartDiff())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            COMPONENT_RECIPE -> {
                val binding = ComponentCartHeadingRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComponentCartRecipe(binding, onCartEvent)
            }
            COMPONENT_AISLE -> {
                val binding = ComponentCartHeadingAisleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComponentCartAisle(binding)
            }
            COMPONENT_INGREDIENT -> {
                val binding = ComponentCartIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComponentCartIngredient(binding, onCartEvent)
            }
            else -> throw IllegalStateException("Invalid view type $viewType rendering")
        }
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val shoppingCart = asyncDiff.currentList[position]
        when(holder.itemViewType) {
            COMPONENT_RECIPE -> {
                val item = shoppingCart as ShoppingCart.RecipeHeading
                (holder as ComponentCartRecipe).bind(item.recipe)
            }
            COMPONENT_AISLE -> {
                val item = shoppingCart as ShoppingCart.AisleCategory
                (holder as ComponentCartAisle).bind(item.title)
            }
            COMPONENT_INGREDIENT -> {
                val item = shoppingCart as ShoppingCart.Ingredient
                (holder as ComponentCartIngredient).bind(item.ingredient)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(asyncDiff.currentList[position]) {
            is ShoppingCart.AisleCategory -> COMPONENT_AISLE
            is ShoppingCart.Ingredient -> COMPONENT_INGREDIENT
            is ShoppingCart.RecipeHeading -> COMPONENT_RECIPE
        }
    }

    fun reload(items: List<ShoppingCart>) {
        asyncDiff.submitList(items)
    }
}