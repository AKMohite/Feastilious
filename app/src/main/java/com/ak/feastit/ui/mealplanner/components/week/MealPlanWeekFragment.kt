package com.ak.feastit.ui.mealplanner.components.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlanWeekBinding

internal class MealPlanWeekFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentMealPlanWeekBinding.inflate(inflater)
    }

    private val binding: FragmentMealPlanWeekBinding
        get() = baseBinding as FragmentMealPlanWeekBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
