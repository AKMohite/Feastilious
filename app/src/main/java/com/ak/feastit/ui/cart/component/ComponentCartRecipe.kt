package com.ak.feastit.ui.cart.component

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentCartHeadingRecipeBinding
import com.ak.feastit.ui.cart.CartRecipe
import com.ak.feastit.utils.onClick

internal class ComponentCartRecipe(
    private val binding: ComponentCartHeadingRecipeBinding,
    private val onCartEvent: (CartEvent) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: CartRecipe) {
        binding.recipeName.text = recipe.name
        binding.recipeImg.load(recipe.img)
        binding.servings.apply {
            context.getString(R.string.servings, recipe.servings)
        }
        binding.removeRecipeBtn.onClick {
            onCartEvent(CartEvent.RemoveRecipe(recipe.id))
        }
    }

}
