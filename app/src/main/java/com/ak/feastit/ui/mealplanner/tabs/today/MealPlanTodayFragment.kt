package com.ak.feastit.ui.mealplanner.tabs.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlanTodayBinding

internal class MealPlanTodayFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? =
        FragmentMealPlanTodayBinding.inflate(inflater)

    private val binding: FragmentMealPlanTodayBinding
        get() = baseBinding as FragmentMealPlanTodayBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
