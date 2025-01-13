package com.ak.feastit.ui.mealplanner.tabs.unscheduled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentUnscheduledRecipesBinding
import com.ak.feastit.ui.mealplanner.MealPlannerViewModel
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.UnscheduledRecipeAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class UnscheduledRecipeFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentUnscheduledRecipesBinding.inflate(inflater)
    }

    private val binding: FragmentUnscheduledRecipesBinding
        get() = baseBinding as FragmentUnscheduledRecipesBinding

    private val viewModel: MealPlannerViewModel by viewModels({ requireParentFragment() })

    private val adapter by lazy {
        UnscheduledRecipeAdapter(
            onMenuClick = { recipeId ->
//                todo show bottom menu options
            }
        )
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.unscheduledRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.unscheduledRecipes.adapter = adapter
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    adapter.reload(state.unscheduledRecipes)
                }
            }
        }
    }

}
