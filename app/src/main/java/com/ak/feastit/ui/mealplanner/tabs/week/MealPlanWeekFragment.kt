package com.ak.feastit.ui.mealplanner.tabs.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlanWeekBinding
import com.ak.feastit.ui.mealplanner.MealPlannerState
import com.ak.feastit.ui.mealplanner.MealPlannerViewModel
import com.ak.feastit.utils.onClick
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class MealPlanWeekFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentMealPlanWeekBinding.inflate(inflater)
    }

    private val binding: FragmentMealPlanWeekBinding
        get() = baseBinding as FragmentMealPlanWeekBinding

    private val viewModel: MealPlannerViewModel by viewModels({ requireParentFragment() })
    private val adapter: MealPlanWeekAdapter by lazy {
        MealPlanWeekAdapter()
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.weeklyRecipes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.weeklyRecipes.adapter = adapter
        binding.nextBtn.onClick { viewModel.onNextWeek() }
        binding.previousBtn.onClick { viewModel.onPreviousWeek() }
        binding.currentBtn.onClick { openDatePicker() }
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

    private fun openDatePicker() {

//        val today = now().epochSeconds
//        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//
//        calendar.timeInMillis = today
//        calendar[Calendar.MONTH] = Calendar.JANUARY
//        val janThisYear = calendar.timeInMillis
//
//        calendar.timeInMillis = today
//        calendar[Calendar.MONTH] = Calendar.DECEMBER
//        val decThisYear = calendar.timeInMillis
//        val constraints =
//            CalendarConstraints.Builder()
//                .setStart(janThisYear)
//                .setEnd(decThisYear)
//                .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.schedule_meal_on))
                .setSelection(viewModel.getSelectedDateEpoch())
//            .setCalendarConstraints(constraints)
                .build()

        datePicker.addOnPositiveButtonClickListener {
            viewModel.onDateSelected(it)
        }
        datePicker.addOnNegativeButtonClickListener {
            // Respond to negative button click.
        }

        datePicker.show(childFragmentManager, "MEAL_DATE_PICKER")
    }

    private fun renderView(state: MealPlannerState) {
        binding.currentBtn.text = state.weekRange
        adapter.reload(state.weeklySections)
    }

}
