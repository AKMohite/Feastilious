package com.ak.feastit.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.data.utils.FeastPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val prefManager: FeastPrefManager
): ViewModel(){
    val liveData: LiveData<OnBoardingState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<OnBoardingState>()
    fun finishOnBoarding() {
        viewModelScope.launch {
            prefManager.updateFirstInstall(false)
            mutableLiveData.postValue(OnBoardingState.NavigateToHome)
        }
    }

    sealed class OnBoardingState {
        object NavigateToHome: OnBoardingState()
    }
}