package com.ak.feastit.ui.cart.component

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentCartHeadingAisleBinding

internal class ComponentCartAisle(
    private val binding: ComponentCartHeadingAisleBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(title: String) {
        binding.aisleCategory.text = title
    }

}
