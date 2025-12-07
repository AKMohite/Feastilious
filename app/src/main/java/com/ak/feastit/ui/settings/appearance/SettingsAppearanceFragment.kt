// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.ak.feastit.R
import com.ak.feastit.utils.doOnApplyWindowInsets
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
internal class SettingsAppearanceFragment : PreferenceFragmentCompat() {

  @Inject lateinit var dispatcherProvider: DispatcherProvider

  private val themes = allAppThemes

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.doOnApplyWindowInsets { insetView, insets, _, margins ->
      val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//        insetView.updatePadding(
//          bottom = margins.bottom + inset + resources.getDimensionPixelSize(R.dimen.bottom_screen_padding)
//        )
      insetView.updatePadding(top = inset.top)
      // TODO screen is not viewed from back of bottom bar
      insetView.updateLayoutParams<MarginLayoutParams> {
        bottomMargin = margins.bottom + inset.bottom + resources.getDimensionPixelSize(R.dimen.bottom_screen_padding)
//        topMargin = margins.top + inset.top
      }
    }
  }

  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?,
  ) {
    preferenceManager.sharedPreferencesName = getString(R.string.settings_preferences_file_name)
    setPreferencesFromResource(R.xml.settings_appearance_preferences, rootKey)

    // region Dynamic theme
    findPreference<SwitchPreferenceCompat>(getString(R.string.preference_key_dynamic_theme))?.apply {
      isVisible = DynamicColors.isDynamicColorAvailable()
      setOnPreferenceChangeListener { _, newValue ->
        findPreference<ThemePickerPreference>(getString(R.string.preference_key_theme))?.apply {
          isEnabled = !((newValue as? Boolean) ?: true)
          canSelect(isEnabled)
        }
        recreateActivity()
        true
      }
    }
    // endregion

    // region Theme picker
    findPreference<ThemePickerPreference>(getString(R.string.preference_key_theme))?.apply {
      setThemeEntries(themes)
      lifecycleScope.launch {
        val theme = getSelectedThemeFromPreference()
        setSelectedTheme(theme)
      }
      setOnPreferenceChangeListener { _, newValue ->
        val theme = newValue as? FeastTheme ?: return@setOnPreferenceChangeListener false
        Timber.d("On theme preference change: ${theme.dataClassName}")
        lifecycleScope.launch(dispatcherProvider.io) {
          preferenceManager.sharedPreferences?.edit(commit = true) {
            putString(getString(R.string.preference_key_theme), theme.dataClassName)
          }
          withContext(dispatcherProvider.main) {
            applyAppTheme(theme)
          }
        }
        true
      }
    }
    // endregion
  }

  private fun applyAppTheme(theme: FeastTheme) {
    requireActivity().setTheme(theme.style)
    recreateActivity()
  }

  private fun recreateActivity() {
    ActivityCompat.recreate(requireActivity())
  }

  override fun onDisplayPreferenceDialog(preference: Preference) {
    val builder = MaterialAlertDialogBuilder(requireContext())
      .setTitle(preference.title)
      .setIcon(preference.icon)
      .setNegativeButton(android.R.string.cancel, null)

    when (preference) {
      is ListPreference -> {
        val selectedIndex = preference.findIndexOfValue(preference.value)
        builder.setSingleChoiceItems(preference.entries, selectedIndex) { dialog, index ->
          val value = preference.entryValues[index].toString()
          if (preference.callChangeListener(value)) {
            preference.value = value
          }
          dialog.dismiss()
        }
      }
    }

    builder.show()
  }

  private suspend fun getSelectedThemeFromPreference(): FeastTheme = withContext(dispatcherProvider.io) {
    val selectedThemeName = preferenceManager.sharedPreferences?.getString(getString(R.string.preference_key_theme), null)
      ?: return@withContext getDefaultTheme()
    themes.firstOrNull { it.dataClassName == selectedThemeName } ?: getDefaultTheme()
  }

  private fun getDefaultTheme(): FeastTheme.Default = themes.first { it is FeastTheme.Default } as FeastTheme.Default
}
