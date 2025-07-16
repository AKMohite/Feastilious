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
  private var selectedTheme: FeastTheme = FeastTheme.Default()

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): ComponentAppearanceTheme {
    val binding = ComponentAppearanceThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ComponentAppearanceTheme(
      binding = binding,
      onThemeClick = {
        onThemeClick.invoke(it)
      },
    )
  }

  override fun onBindViewHolder(
    holder: ComponentAppearanceTheme,
    position: Int,
  ) {
    holder.bind(asyncDiff.currentList[position], selectedTheme)
  }

  override fun getItemCount(): Int = asyncDiff.currentList.size

  fun submitList(themes: List<FeastTheme>) {
    asyncDiff.submitList(themes)
  }

  private fun getAllThemes() = asyncDiff.currentList.toList()
  fun setSelectedTheme(theme: FeastTheme) {
    val prevSelectedIndex = getAllThemes().indexOf(selectedTheme)
    val newSelectedIndex = getAllThemes().indexOf(theme)
    selectedTheme = theme
    if (prevSelectedIndex != -1) {
      notifyItemChanged(prevSelectedIndex)
    }
    if (newSelectedIndex != -1) {
      notifyItemChanged(newSelectedIndex)
    }
  }
}
