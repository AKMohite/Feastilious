package com.ak.feastit.ui.yumdetail.components.ingredients.component

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeIngredientBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.Ingredient

class IngredientViewHolder(
    private val binding: ComponentRecipeIngredientBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: Ingredient) {
//        binding.imgIngredient.load(ingredient.image)
        val str = SpannableStringBuilder()
            .bold { append(ingredient.quantity) }
            .append(" ")
            .append(ingredient.name)
        binding.tvIngredientDesc.text = str
        binding.toggleCartBtn.onClick {  }
    }
}

