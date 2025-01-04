package com.ak.feastit.ui.yumdetail.components.ingredients.component

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.R
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
        if (ingredient.isBought) {
            val strikethroughSpan = StrikethroughSpan()
            str.setSpan(
                strikethroughSpan,
                0,  // Start
                str.length,  // End (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Text changes will not reflect in the strike changing
            )
        }
        binding.tvIngredientDesc.text = str
        val cartIcon = if (ingredient.isInCart) {
            R.drawable.ic_remove_circle
        } else {
            R.drawable.ic_add_circle
        }
        with(binding.toggleCartBtn) {
            icon = ContextCompat.getDrawable(this.context, cartIcon)
            onClick {  }
        }
    }
}

