// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.ComponentOnboardingItemBinding

open class ComponentPagerItem : BaseFragment() {
  @StringRes
  protected var title: Int? = null

  @StringRes
  protected var subtitle: Int? = null

  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = ComponentOnboardingItemBinding.inflate(inflater)

  private val binding: ComponentOnboardingItemBinding
    get() = baseBinding as ComponentOnboardingItemBinding

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    binding.pagerTitle.text = getString(title ?: throw IllegalStateException("No title found"))
    binding.pagerSubTitle.text =
      getString(subtitle ?: throw IllegalStateException("No subtitle found"))
  }

  companion object {
    fun newInstance(
      @StringRes title: Int,
      @StringRes subtitle: Int,
    ) = ComponentPagerItem().apply {
      this.title = title
      this.subtitle = subtitle
    }
  }
}
