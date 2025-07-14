// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentAppearanceThemeBinding

internal class AppearanceThemeAdapter(
  private val onThemeClick: (FeastTheme) -> Unit,
) : RecyclerView.Adapter<ComponentAppearanceTheme>() {

  private val asyncDiff = AsyncListDiffer(this, AppearanceThemeDiff())

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): ComponentAppearanceTheme {
    val binding = ComponentAppearanceThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ComponentAppearanceTheme(binding, onThemeClick)
  }

  override fun onBindViewHolder(
    holder: ComponentAppearanceTheme,
    position: Int,
  ) {
    holder.bind(asyncDiff.currentList[position])
  }

  override fun getItemCount(): Int = asyncDiff.currentList.size

  fun submitList(themes: List<FeastTheme>) {
    asyncDiff.submitList(themes)
  }
}
