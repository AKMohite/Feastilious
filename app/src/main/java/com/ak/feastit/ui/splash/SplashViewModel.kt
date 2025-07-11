// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.splash

import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.data.utils.FeastPrefManager
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
internal class SplashViewModel
@Inject
constructor(
  prefManager: FeastPrefManager,
  dispatcher: DispatcherProvider,
) : BaseViewModel(dispatcher) {
  private val mutableSplashState = MutableStateFlow<SplashState>(SplashState.Empty)
  val splashState: StateFlow<SplashState>
    get() = mutableSplashState.asStateFlow()

  init {
    prefManager.preferencesFlow
//            .debounce(2_500)
      .onEach { prefs ->
        if (prefs.isFirstInstall) {
          mutableSplashState.update { SplashState.NavigateToOnBoarding }
        } else {
          mutableSplashState.update { SplashState.NavigateToHome }
        }
      }.launchIn(uiScope)

//        uiScope.launch {
// //            delay(2_500)
//            prefManager.preferencesFlow
//            .debounce(2_500)
//                .collectLatest { prefs ->
//                    if (prefs.isFirstInstall) {
//                        mutableSplashState.value = SplashState.NavigateToOnBoarding
//                    } else {
//                        mutableSplashState.value = SplashState.NavigateToHome
//                    }
//                }
//        }
  }

  sealed class SplashState {
    data object NavigateToHome : SplashState()

    data object NavigateToOnBoarding : SplashState()

    data object Empty : SplashState()
  }
}
