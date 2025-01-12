package com.ak.feastit.ui.mealplanner.components.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlanWeekBinding
import com.ak.feastit.ui.mealplanner.MealPlannerState
import com.ak.feastit.ui.mealplanner.MealPlannerViewModel
import com.ak.feastit.utils.onClick
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class MealPlanWeekFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentMealPlanWeekBinding.inflate(inflater)
    }

    private val binding: FragmentMealPlanWeekBinding
        get() = baseBinding as FragmentMealPlanWeekBinding

    private val viewModel: MealPlannerViewModel by viewModels({ requireParentFragment() })

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.nextBtn.onClick { viewModel.onNextWeek() }
        binding.previousBtn.onClick { viewModel.onPreviousWeek() }
        binding.currentBtn.onClick {  }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    renderView(state)
                }
            }
        }
    }

    private fun renderView(state: MealPlannerState) {
        binding.currentBtn.text = state.weekRange
    }

}
