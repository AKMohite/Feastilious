// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.databinding.FragmentSettingsAppearanceBinding

internal class ThemePickerPreference(
  context: Context,
  attrs: AttributeSet,
) : Preference(context, attrs) {

  private var baseBinding: FragmentSettingsAppearanceBinding? = null
  private val binding: FragmentSettingsAppearanceBinding
    get() = baseBinding!!
  private var themeAdapter: AppearanceThemeAdapter? = null

  override fun onBindViewHolder(holder: PreferenceViewHolder) {
    super.onBindViewHolder(holder)
    baseBinding = FragmentSettingsAppearanceBinding.bind(holder.itemView)
    binding.appearanceThemes.apply {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
      adapter = getThemePickerAdapter()
    }
  }

  fun setThemeEntries(
    themeEntries: List<FeastTheme>,
  ) {
    getThemePickerAdapter()?.submitList(themeEntries)
  }

  override fun onDetached() {
    baseBinding = null
    themeAdapter = null
    super.onDetached()
  }

  private fun getThemePickerAdapter(): AppearanceThemeAdapter? {
    if (themeAdapter == null) {
      themeAdapter = AppearanceThemeAdapter {
//      recreate activity
        if (callChangeListener(it)) {
          themeAdapter?.setSelectedTheme(it)
        }
      }
    }
    return themeAdapter
  }

  fun setSelectedTheme(theme: FeastTheme) {
    themeAdapter?.setSelectedTheme(theme)
  }

  fun canSelect(enabled: Boolean) {
    binding.root.isEnabled = enabled
  }
}
