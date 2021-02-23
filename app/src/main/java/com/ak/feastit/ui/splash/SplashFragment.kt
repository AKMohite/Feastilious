package com.ak.feastit.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ak.feastit.R
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.NavigateToHome
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.NavigateToOnBoarding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.splash_fragment) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NavigateToOnBoarding -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
                }
                is NavigateToHome -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRecipeDashboardFragment())
                }
            }
        })
    }

}