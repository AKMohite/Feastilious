// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.settings.appearance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentSettingsAppearanceBinding
import com.ak.feastit.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
internal class SettingsAppearanceFragment : BaseFragment() {

  private val viewModel: SettingsViewModel by viewModels()
  private var adapter: AppearanceThemeAdapter? = null

  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentSettingsAppearanceBinding.inflate(inflater)
  private val binding: FragmentSettingsAppearanceBinding
    get() = baseBinding as FragmentSettingsAppearanceBinding

  override fun onViewReady(view: View, savedInstanceState: Bundle?) {
    setupView()
    observers()
  }

  private fun setupView() {
    binding.appearanceThemes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    adapter = AppearanceThemeAdapter(
      onThemeClick = { theme -> themeChanged(theme) },
    ).apply {
      stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
    binding.appearanceThemes.adapter = adapter
  }

  private fun themeChanged(theme: FeastTheme) {
    Timber.d("Theme changed to: ${context?.getString(theme.title)}")
    viewModel.updateTheme(theme)
//    TODO set theme before activity super.onCreate()
    /*val hostActivity = requireActivity()
    hostActivity.setTheme(theme.style)
    hostActivity.recreate()*/
  }

  private fun observers() {
    viewModel.loadThemes()
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.appearanceThemes.collectLatest { themes ->
          adapter?.submitList(themes)
        }
      }
    }
  }

  override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
  }
}
