package com.ak.feastit.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ak.feastit.R
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.splash_fragment) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
TODO(https://www.youtube.com/watch?v=bbvYidjZ_CE)
        setFlowObservers()
    }

    private fun setFlowObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
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