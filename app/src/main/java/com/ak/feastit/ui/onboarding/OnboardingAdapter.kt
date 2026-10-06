// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(
  private val pagerComponents: List<Fragment>,
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
  override fun getItemCount(): Int = 3

  override fun createFragment(position: Int): Fragment = pagerComponents[position]
}
