package com.ak.feastit.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.SplashFragmentBinding
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.Empty
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.NavigateToHome
import com.ak.feastit.ui.splash.SplashViewModel.SplashState.NavigateToOnBoarding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class SplashFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        SplashFragmentBinding.inflate(inflater)

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        //TODO(https://www.youtube.com/watch?v=bbvYidjZ_CE)
        setFlowObservers()
    }

    private fun setFlowObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.splashState.collect { state ->
                    when (state) {
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