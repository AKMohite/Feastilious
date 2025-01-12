package com.ak.feastit.ui.mealplanner.components.unscheduled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentUnscheduledRecipesBinding

internal class UnscheduledRecipeFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentUnscheduledRecipesBinding.inflate(inflater)
    }

    private val binding: FragmentUnscheduledRecipesBinding
        get() = baseBinding as FragmentUnscheduledRecipesBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
