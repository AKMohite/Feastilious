package com.ak.feastit.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.data.utils.FeastPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val prefManager: FeastPrefManager
): ViewModel(){

    private val _state: MutableStateFlow<OnBoardingState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
        .filterNotNull()

    fun finishOnBoarding() {
        viewModelScope.launch {
            prefManager.updateFirstInstall(false)
            _state.update { OnBoardingState.NavigateToHome }
        }
    }

    sealed class OnBoardingState {
        data object NavigateToHome: OnBoardingState()
    }
}