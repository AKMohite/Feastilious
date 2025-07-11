// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentOnBoardingBinding
import com.ak.feastit.ui.onboarding.OnBoardingViewModel.OnBoardingState.NavigateToHome
import com.ak.feastit.utils.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentOnBoardingBinding.inflate(inflater)

  private val binding: FragmentOnBoardingBinding
    get() = baseBinding as FragmentOnBoardingBinding

  private val viewModel: OnBoardingViewModel by viewModels()

  private val pagerCallback =
    object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        setCurrentOnboardingIndicators(position)
      }
    }

  private val adapter: OnboardingAdapter by lazy {
    OnboardingAdapter(getPagerComponents(), childFragmentManager, viewLifecycleOwner.lifecycle)
  }

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    setupView()
    observers()
  }

  private fun setupView() {
    binding.onboardingPager.adapter = adapter
    binding.onboardingPager.registerOnPageChangeCallback(pagerCallback)
    binding.navigateHome.onClick {
      viewModel.finishOnBoarding()
    }
    setOnboardingIndicator()
    setCurrentOnboardingIndicators(0)
    binding.navigateNext.onClick {
      if (binding.onboardingPager.currentItem + 1 < adapter.getItemCount()) {
        binding.onboardingPager.currentItem += 1
      } else {
        viewModel.finishOnBoarding()
      }
    }
  }

  private fun observers() {
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collectLatest { state ->
          when (state) {
            NavigateToHome -> {
              findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToRecipeDashboardFragment())
            }
          }
        }
      }
    }
  }

  private fun setOnboardingIndicator() {
    binding.onboardingProgressIndicators.removeAllViews()
    val indicators: Array<ImageView?> = Array<ImageView?>(3) { null }
    val layoutParams =
      LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
      )
    layoutParams.setMargins(8, 0, 8, 0)
    for (i in 0 until adapter.itemCount) {
      val imageView = ImageView(requireContext())
      imageView.setImageDrawable(
        ContextCompat.getDrawable(
          requireContext(),
          R.drawable.onboarding_indicator_active,
        ),
      )
      imageView.layoutParams = layoutParams
      indicators[i] = imageView
      binding.onboardingProgressIndicators.addView(indicators[i])
    }
  }

  private fun setCurrentOnboardingIndicators(index: Int) {
    val childCount = binding.onboardingProgressIndicators.childCount
    for (i in 0 until childCount) {
      val imageView = binding.onboardingProgressIndicators.getChildAt(i) as? ImageView
      if (i == index) {
        imageView?.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.onboarding_indicator_active,
          ),
        )
      } else {
        imageView?.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.onboarding_indicator_inactive,
          ),
        )
      }
    }
    if (index == adapter.getItemCount() - 1) {
      binding.navigateNext.text = getString(R.string.get_started)
    } else {
      binding.navigateNext.text = getString(R.string.next)
    }
  }

  private fun getPagerComponents(): List<Fragment> = listOf(
    ComponentPagerItem.newInstance(
      R.string.onboarding_title_1,
      R.string.onboarding_sub_title_1,
    ),
    ComponentPagerItem.newInstance(
      R.string.onboarding_title_2,
      R.string.onboarding_sub_title_2,
    ),
    ComponentPagerItem.newInstance(
      R.string.onboarding_title_3,
      R.string.onboarding_sub_title_3,
    ),
  )

  override fun onDestroyView() {
    binding.onboardingPager.unregisterOnPageChangeCallback(pagerCallback)
    super.onDestroyView()
  }
}
