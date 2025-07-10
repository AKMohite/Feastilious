package com.ak.feastit.ui.cart.component

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentCartIngredientBinding
import com.ak.feastit.utils.onClick
import com.mak.feastit.domain.model.CartIngredient

internal class ComponentCartIngredient(
    private val binding: ComponentCartIngredientBinding,
    private val onCartEvent: (CartEvent) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: CartIngredient) {
        val str = SpannableStringBuilder()
            .bold { append(ingredient.quantity) }
            .append(" ")
            .append(ingredient.ingredientName)
        if (ingredient.isBought) {
            val strikethroughSpan = StrikethroughSpan()
            str.setSpan(
                strikethroughSpan,
                0,  // Start
                str.length,  // End (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Text changes will not reflect in the strike changing
            )
        }
        binding.ingredientName.text = str
        binding.cbBuy.isChecked = ingredient.isBought
        binding.root.onClick { onCartEvent(CartEvent.ToggleIngredient(ingredient.id)) }
//        binding.cbBuy.setOnCheckedChangeListener { _, _ ->
//            onCartEvent(CartEvent.ToggleIngredient(ingredient.id))
//        }
    }

}
