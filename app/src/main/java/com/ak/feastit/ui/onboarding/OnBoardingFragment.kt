package com.ak.feastit.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ak.feastit.R
import com.ak.feastit.databinding.FragmentOnBoardingBinding
import com.ak.feastit.ui.onboarding.OnBoardingViewModel.OnBoardingState.NavigateToHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOnBoardingBinding.bind(view)
        binding.navigateHome.setOnClickListener {
            viewModel.finishOnBoarding()
        }
        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                NavigateToHome -> {
                    findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToRecipeDashboardFragment())
                }
            }
        })
    }

}