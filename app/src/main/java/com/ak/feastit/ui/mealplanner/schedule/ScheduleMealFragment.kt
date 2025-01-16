package com.ak.feastit.ui.mealplanner.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import coil.load
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentScheduleMealBinding
import com.ak.feastit.utils.hide
import com.ak.feastit.utils.onClick
import com.ak.feastit.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ScheduleMealFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentScheduleMealBinding.inflate(inflater)

    private val binding: FragmentScheduleMealBinding =
        baseBinding as FragmentScheduleMealBinding

    private val viewModel: ScheduleMealViewmodel by viewModels()

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.mealPlan.moreMenu.hide()
        binding.scheduleDate.onClick {
//            openDatePicker
        }
        binding.preparationTime.onClick {
//            openTimePicker
        }
        binding.servingTime.onClick {
//            openTimePicker
        }
//        binding.addToCalendar.onClick {  }
        binding.submit.onClick {
            viewModel.submit(binding.addToCalendar.isChecked)
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    renderView(state)

                }
            }
        }
    }

    private fun renderView(state: ScheduleMealState) {
        binding.mainContainer.show(state.mealPlan != null)
        val mealPlan = state.mealPlan ?: return
        binding.mealPlan.recipeName.text = mealPlan.name
        binding.mealPlan.preparationTime.text = state.preparationTime
        binding.mealPlan.recipeImg.load(mealPlan.image)
        binding.scheduleDate.text = state.scheduleDate
        binding.preparationTime.text = state.preparationTime
        binding.servingTime.text = state.serveTime
    }
}