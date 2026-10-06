// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentThemeCardBinding
import com.ak.feastit.utils.show

internal class ComponentThemeCard @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defaultStyleAttrs: Int = 0,
  @StyleRes defaultStyleRes: Int = R.style.Theme_FeastIt,
) : FrameLayout(context, attrs, defaultStyleAttrs, defaultStyleRes) {

  private val binding: ComponentThemeCardBinding = ComponentThemeCardBinding.inflate(LayoutInflater.from(context), this, true)

  fun bind(theme: FeastTheme, selectedTheme: FeastTheme) {
    binding.name.text = context.getString(theme.title)
    binding.themeCardSelected.show(theme.dataClassName == selectedTheme.dataClassName)
//    binding.root.theme = theme.theme
  }
}
