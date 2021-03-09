package com.ak.feastit.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.data.utils.FeastPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefManager: FeastPrefManager
) : ViewModel() {
    val splashState: StateFlow<SplashState>
        get() = mutableSplashState
    private val mutableSplashState = MutableStateFlow<SplashState>(SplashState.Empty)
    init {
        viewModelScope.launch {
            delay(3000)
            prefManager.preferencesFlow.collect { prefs ->
                if (prefs.isFirstInstall) {
                    mutableSplashState.value = SplashState.NavigateToOnBoarding
                } else {
                    mutableSplashState.value = SplashState.NavigateToHome
                }
            }
        }
    }

    sealed class SplashState {
        object NavigateToHome : SplashState()
        object NavigateToOnBoarding : SplashState()
        object Empty : SplashState()
    }
}