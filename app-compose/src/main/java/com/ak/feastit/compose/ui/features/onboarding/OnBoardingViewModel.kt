// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.onboarding

import com.ak.feastit.compose.base.BaseViewModel
import com.ak.feastit.compose.data.utils.FeastPrefManager
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class OnBoardingViewModel
@Inject
constructor(
  private val prefManager: FeastPrefManager,
  dispatcher: DispatcherProvider,
) : BaseViewModel(dispatcher) {
  private val _state: MutableStateFlow<OnBoardingState?> = MutableStateFlow(null)
  val state =
    _state
      .asStateFlow()
      .filterNotNull()

  fun finishOnBoarding() {
    uiScope.launch {
      prefManager.updateFirstInstall(false)
      _state.update { OnBoardingState.NavigateToHome }
    }
  }

  sealed class OnBoardingState {
    data object NavigateToHome : OnBoardingState()
  }
}
