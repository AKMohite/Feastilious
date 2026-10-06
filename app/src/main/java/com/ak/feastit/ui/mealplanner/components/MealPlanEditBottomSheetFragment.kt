// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.databinding.FragmentMealPlanEditBinding
import com.ak.feastit.ui.mealplanner.MealPlanRecipeSheetItem
import com.ak.feastit.ui.mealplanner.MealPlannerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MealPlanEditBottomSheetFragment : BottomSheetDialogFragment() {

  private var baseBinding: FragmentMealPlanEditBinding? = null
  private val binding: FragmentMealPlanEditBinding
    get() = baseBinding!!
  private val viewModel: MealPlannerViewModel by viewModels({ requireParentFragment() })
  private var adapter: MealPlanSheetMenuAdapter? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    baseBinding = FragmentMealPlanEditBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView()
    observers()
  }

  private fun setupView() {
    binding.editMealPlanList.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    adapter = MealPlanSheetMenuAdapter(
      onMenuClick = { item ->
        handleMenuClick(item)
      },
    )
    binding.editMealPlanList.adapter = adapter
  }

  private fun observers() {
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect { state ->
          adapter?.reload(state.menuItems)
        }
      }
    }
  }

  private fun handleMenuClick(item: MealPlanRecipeSheetItem) {
    dismiss()
    viewModel.onMealPlanMenuClick(item)
  }

  override fun onDestroyView() {
    adapter = null
    baseBinding = null
    super.onDestroyView()
  }

  companion object {
    fun show(fragmentManager: FragmentManager) {
      MealPlanEditBottomSheetFragment().show(
        fragmentManager,
        MealPlanEditBottomSheetFragment::class.java.name,
      )
    }
  }
}
