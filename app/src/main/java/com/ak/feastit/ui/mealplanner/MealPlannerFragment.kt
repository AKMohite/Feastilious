package com.ak.feastit.ui.mealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentMealPlannerBinding
import com.ak.feastit.utils.onClick
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

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {

    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->

                }
            }
        }
    }
}