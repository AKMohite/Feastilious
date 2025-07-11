// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import coil.load
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentScheduleMealBinding
import com.ak.feastit.utils.hide
import com.ak.feastit.utils.onClick
import com.ak.feastit.utils.show
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ScheduleMealFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentScheduleMealBinding.inflate(inflater)

  private val binding: FragmentScheduleMealBinding
    get() = baseBinding as FragmentScheduleMealBinding

  private val viewModel: ScheduleMealViewmodel by viewModels()

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    setupView()
    observers()
  }

  private fun setupView() {
    binding.mealPlan.moreMenu.hide()
    binding.scheduleDateBtn.onClick {
      openDatePicker()
    }
//        binding.preparationTimeBtn.onClick {
//            openTimePicker()
//        }
    binding.servingTimeBtn.onClick {
      openTimePicker()
    }
    binding.addToCalendar.onClick { }
    binding.submitBtn.onClick {
      viewModel.submit(binding.addToCalendar.isChecked)
    }
  }

  private fun observers() {
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.state.collect { state ->
            renderView(state)
          }
        }
        launch {
          viewModel.action.collectLatest { action ->
            handleActions(action)
          }
        }
      }
    }
  }

  private fun handleActions(action: ScheduleMealAction) {
    when (action) {
      ScheduleMealAction.OnMealScheduled -> findNavController().navigateUp()
    }
  }

  private fun renderView(state: ScheduleMealState) {
    binding.mainContainer.show(state.mealPlan != null)
    val mealPlan = state.mealPlan ?: return
    binding.mealPlan.recipeName.text = mealPlan.name
    binding.mealPlan.preparationTime.text = mealPlan.displayablePreparationTime()
    binding.mealPlan.recipeImg.load(mealPlan.image)
    binding.scheduleDateBtn.text = state.scheduleDate
    binding.preparationTimeBtn.text = state.preparationTime
    binding.servingTimeBtn.text = state.serveTime
  }

  private fun openDatePicker() {
    val datePicker =
      MaterialDatePicker.Builder
        .datePicker()
        .setTitleText(getString(R.string.schedule_meal_on))
        .setSelection(viewModel.getSelectedDateEpoch())
//            .setCalendarConstraints(constraints)
        .build()

    datePicker.addOnPositiveButtonClickListener {
      viewModel.onDateSelected(it)
    }
    datePicker.addOnNegativeButtonClickListener {}

    datePicker.show(childFragmentManager, "MEAL_DATE_PICKER")
  }

  private fun openTimePicker() {
//        val isSystem24Hour: Boolean = DateFormat.is24HourFormat(context)
//        val clockFormat = if (isSystem24Hour) CLOCK_24H else CLOCK_12H
    val (hour, minute) = viewModel.getHourMinute()
    val materialTimePickerBuilder =
      MaterialTimePicker
        .Builder()
        .setTimeFormat(CLOCK_24H)
        .setHour(hour)
        .setMinute(minute)

//        if (timeInputMode != null) {
//            materialTimePickerBuilder.setInputMode(timeInputMode)
//        }

    val materialTimePicker = materialTimePickerBuilder.build()
    materialTimePicker.clearOnPositiveButtonClickListeners()
    materialTimePicker.addOnPositiveButtonClickListener { dialog: View? ->
      val newHour = materialTimePicker.hour
      val newMinute = materialTimePicker.minute
      viewModel.onTimeSet(newHour, newMinute)
    }
    materialTimePicker.showNow(childFragmentManager, "MEAL_TIME_PICKER")
  }
}
