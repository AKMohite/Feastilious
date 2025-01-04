package com.ak.feastit.ui.yumdetail.components.ingredients.component

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.databinding.ComponentRecipeIngredientBinding
import com.mak.feastit.domain.model.Ingredient

class IngredientViewHolder(
    private val binding: ComponentRecipeIngredientBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: Ingredient) {
//        binding.imgIngredient.load(ingredient.image)
        binding.tvIngredientDesc.text = ingredient.name
    }
}
