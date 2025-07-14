// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import android.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentAppearanceThemeBinding
import com.ak.feastit.utils.onClick

internal class ComponentAppearanceTheme(
  private val binding: ComponentAppearanceThemeBinding,
  private val onThemeClick: (FeastTheme) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

  fun bind(theme: FeastTheme) {
    binding.root.removeAllViews()
    val contextTheme = ContextThemeWrapper(binding.root.context, theme.style)
    val themeComponent = ComponentThemeCard(context = contextTheme, defaultStyleRes = theme.style).apply {
      bind(theme)
    }
    binding.root.addView(themeComponent)
    binding.root.onClick { onThemeClick(theme) }
  }
}
