// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.ingredients.component

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentHeaderButtonBinding
import com.ak.feastit.utils.onClick

class ComponentHeaderButton(
  private val binding: ComponentHeaderButtonBinding,
  private val onToggleAddToCart: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(title: Int) {
    binding.headerBtn.text = binding.root.context.getString(title)
    binding.headerBtn.onClick { onToggleAddToCart() }
  }
}
