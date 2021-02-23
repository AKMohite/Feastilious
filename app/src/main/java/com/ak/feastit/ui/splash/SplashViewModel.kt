package com.ak.feastit.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.data.utils.FeastPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefManager: FeastPrefManager
) : ViewModel() {
    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()
    init {
        viewModelScope.launch {
            delay(3000)
            prefManager.preferencesFlow.collect { prefs ->
                if (prefs.isFirstInstall) {
                    mutableLiveData.postValue(SplashState.NavigateToOnBoarding)
                } else {
                    mutableLiveData.postValue(SplashState.NavigateToHome)
                }
            }
        }
    }

    sealed class SplashState {
        object NavigateToHome : SplashState()
        object NavigateToOnBoarding : SplashState()
    }
}