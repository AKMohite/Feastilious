package com.ak.feastit.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ak.feastit.R
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.Empty
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.NavigateToHome
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.NavigateToOnBoarding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.splash_fragment) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO(https://www.youtube.com/watch?v=bbvYidjZ_CE)
        setFlowObservers()
    }

    private fun setFlowObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.splashState.collect {
                    when (it) {
                        NavigateToOnBoarding -> {
                            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
                        }
                        NavigateToHome -> {
                            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRecipeDashboardFragment())
                        }
                        Empty -> {}
                    }
                }
            }
        }
    }

}