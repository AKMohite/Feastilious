// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.settings

import com.ak.feastit.compose.base.BaseViewModel
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
  dispatcher: DispatcherProvider,
) : BaseViewModel(dispatcher) {

  private val _appearanceThemes = MutableStateFlow(emptyList<FeastTheme>())
  val appearanceThemes = _appearanceThemes.asStateFlow()

  fun loadThemes() {
    uiScope.launch {
      _appearanceThemes.update { allAppThemes }
    }
  }

  fun updateTheme(theme: FeastTheme) {
//    TODO store in preference
  }
}
