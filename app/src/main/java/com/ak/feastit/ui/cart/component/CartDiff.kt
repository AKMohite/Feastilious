// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.cart.component

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.cart.ShoppingCart

internal class CartDiff : DiffUtil.ItemCallback<ShoppingCart>() {
  override fun areItemsTheSame(
    oldItem: ShoppingCart,
    newItem: ShoppingCart,
  ): Boolean = oldItem.areItemsTheSame(newItem)

  override fun areContentsTheSame(
    oldItem: ShoppingCart,
    newItem: ShoppingCart,
  ): Boolean = oldItem.areContentsTheSame(newItem)
}
