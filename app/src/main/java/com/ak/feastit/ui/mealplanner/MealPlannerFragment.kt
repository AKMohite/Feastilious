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
import com.ak.feastit.ui.mealplanner.components.MealPlanSheetMenuAction
import com.ak.feastit.ui.mealplanner.tabs.MealPlanPagerAdapter
import com.ak.feastit.ui.mealplanner.tabs.MealPlanTab
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MealPlannerFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentMealPlannerBinding.inflate(inflater)

    private val binding: FragmentMealPlannerBinding
        get() = baseBinding as FragmentMealPlannerBinding

    private val viewModel: MealPlannerViewModel by viewModels()

    private val tabLayoutMediator: TabLayoutMediator by lazy {
        TabLayoutMediator(
            binding.mealPlanTabs,
            binding.mealPlanPager
        ) { tab, position ->
            tab.text = when(position) {
                MealPlanTab.Today.ordinal -> getString(R.string.today)
                MealPlanTab.Week.ordinal -> getString(R.string.week)
                MealPlanTab.UnscheduledRecipes.ordinal -> getString(R.string.unscheduled_recipes)
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }
    }

    private val pagerAdapter: MealPlanPagerAdapter by lazy {
        MealPlanPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.mealPlanPager.adapter = pagerAdapter
        tabLayoutMediator.attach()
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collectLatest { action -> handleActions(action)}
            }
        }
    }

    private fun handleActions(action: MealPlanAction) {
        when(action) {
            is MealPlanAction.OpenMealPlanBottomSheet -> {
                findNavController().navigate(MealPlannerFragmentDirections.plannerToEditDialog(action.recipeId))
            }
            is MealPlanAction.OnMenuClick -> handleBottomSheetAction(action.item)
        }
    }

    private fun handleBottomSheetAction(action: MealPlanSheetMenuAction) {
        when(action) {
            MealPlanSheetMenuAction.DOWNLOAD_RECIPE -> {}
            MealPlanSheetMenuAction.SHARE_RECIPE -> {}
            MealPlanSheetMenuAction.ADD_TO_SHOPPING_LIST -> {}
            MealPlanSheetMenuAction.REPEAT_AGAIN -> {}
            MealPlanSheetMenuAction.SET_SCHEDULE -> {}
            MealPlanSheetMenuAction.EDIT_SCHEDULE -> {}
            MealPlanSheetMenuAction.REMOVE_FROM_MEAL_PLAN -> {}
        }
    }

    override fun onDestroyView() {
        tabLayoutMediator.detach()
        super.onDestroyView()
    }
}