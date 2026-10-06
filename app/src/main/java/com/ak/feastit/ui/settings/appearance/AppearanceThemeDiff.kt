// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import androidx.recyclerview.widget.DiffUtil

internal class AppearanceThemeDiff : DiffUtil.ItemCallback<FeastTheme>() {
  override fun areItemsTheSame(
    oldItem: FeastTheme,
    newItem: FeastTheme,
  ): Boolean {
    return oldItem == newItem
  }

  override fun areContentsTheSame(
    oldItem: FeastTheme,
    newItem: FeastTheme,
  ): Boolean {
    return oldItem.style == newItem.style && oldItem.title == newItem.title
  }
}
