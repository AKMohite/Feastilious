package com.ak.feastit.ui.onboarding

import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.data.utils.FeastPrefManager
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OnBoardingViewModel @Inject constructor(
    private val prefManager: FeastPrefManager,
    dispatcher: DispatcherProvider
): BaseViewModel(dispatcher){

    private val _state: MutableStateFlow<OnBoardingState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
        .filterNotNull()

    fun finishOnBoarding() {
        uiScope.launch {
            prefManager.updateFirstInstall(false)
            _state.update { OnBoardingState.NavigateToHome }
        }
    }

    sealed class OnBoardingState {
        data object NavigateToHome: OnBoardingState()
    }
}