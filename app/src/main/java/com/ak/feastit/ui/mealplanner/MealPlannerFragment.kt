package com.ak.feastit.ui.mealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlannerBinding
import com.ak.feastit.ui.mealplanner.components.MealPlanPagerAdapter
import com.ak.feastit.ui.mealplanner.components.MealPlanTab
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

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
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.state.collectLatest { state ->
//
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        tabLayoutMediator.detach()
        super.onDestroyView()
    }
}