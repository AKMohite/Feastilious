// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ak.feastit.BuildConfig
import com.ak.feastit.R
import com.ak.feastit.utils.doOnApplyWindowInsets
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SettingsPreferenceFragment : PreferenceFragmentCompat() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//    returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
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
    setPreferencesFromResource(R.xml.settings_preferences, rootKey)

    // region Appearance
    findPreference<Preference>(getString(R.string.preference_key_appearance))?.setOnPreferenceClickListener {
      findNavController().navigate(SettingsPreferenceFragmentDirections.settingsToAppearance())
      true
    }
    // endregion

    // region app version
    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    findPreference<Preference>(getString(R.string.preference_key_app_version))?.apply {
      summary = appVersion + System.lineSeparator() + getString(R.string.settings_app_version_summary)
      setOnPreferenceClickListener {
        checkForNewVersion()
        true
      }
    }
    // endregion
  }

  private fun checkForNewVersion() {
    Snackbar.make(requireView(), "To implement: New version api call", Snackbar.LENGTH_SHORT).show()
  }
}
