// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlannerBinding
import com.ak.feastit.ui.mealplanner.components.MealPlanEditBottomSheetFragment
import com.ak.feastit.ui.mealplanner.tabs.MealPlanPagerAdapter
import com.ak.feastit.ui.mealplanner.tabs.MealPlanTab
import com.google.android.material.tabs.TabLayoutMediator
import com.mak.feastit.domain.model.MealPlanRecipe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MealPlannerFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentMealPlannerBinding.inflate(inflater)

  private val binding: FragmentMealPlannerBinding
    get() = baseBinding as FragmentMealPlannerBinding

  private val viewModel: MealPlannerViewModel by viewModels()

  private var tabLayoutMediator: TabLayoutMediator? = null

  private var pagerAdapter: MealPlanPagerAdapter? = null

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    setupView()
    observers()
  }

  private fun setupView() {
    tabLayoutMediator =
      TabLayoutMediator(
        binding.mealPlanTabs,
        binding.mealPlanPager,
      ) { tab, position ->
        tab.text =
          when (position) {
            MealPlanTab.Today.ordinal -> getString(R.string.today)
            MealPlanTab.Week.ordinal -> getString(R.string.week)
            MealPlanTab.UnscheduledRecipes.ordinal -> getString(R.string.unscheduled_recipes)
            else -> throw IllegalArgumentException("Invalid tab position: $position")
          }
      }
    pagerAdapter = MealPlanPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
    binding.mealPlanPager.adapter = pagerAdapter
    tabLayoutMediator?.attach()
  }

  private fun observers() {
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.action.collectLatest { action -> handleActions(action) }
      }
    }
  }

  private fun handleActions(action: MealPlanAction) {
    when (action) {
      is MealPlanAction.OpenMealPlanBottomSheet -> {
//                findNavController().navigate(MealPlannerFragmentDirections.plannerToEditDialog(action.recipeId))
//                TODO cannot use nav controller to navigate to bottom sheet to have parent viewmodel?
        MealPlanEditBottomSheetFragment.show(childFragmentManager)
      }

      is MealPlanAction.OnMenuClick -> handleBottomSheetAction(action.item, action.mealPlan)
      is MealPlanAction.OnMealRepeat -> gotoMealSchedule(action.mealId)
    }
  }

  private fun handleBottomSheetAction(
    action: MealPlanSheetMenuAction,
    mealPlan: MealPlanRecipe,
  ) {
    when (action) {
      MealPlanSheetMenuAction.DOWNLOAD_RECIPE -> {}
      MealPlanSheetMenuAction.SHARE_RECIPE -> {}
      MealPlanSheetMenuAction.REPEAT_AGAIN -> {}
      MealPlanSheetMenuAction.SET_SCHEDULE -> gotoMealSchedule(mealPlan.id)
      MealPlanSheetMenuAction.EDIT_SCHEDULE -> gotoMealSchedule(mealPlan.id)
      else -> throw IllegalStateException("This action is not supported in view: $action")
    }
  }

  private fun gotoMealSchedule(id: Long) {
    findNavController().navigate(MealPlannerFragmentDirections.plannerToScheduleMeal(id))
  }

  override fun onDestroyView() {
    tabLayoutMediator?.detach()
    pagerAdapter = null
    tabLayoutMediator = null
    super.onDestroyView()
  }
}
