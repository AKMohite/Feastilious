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
import com.ak.feastit.ui.mealplanner.tabs.MealPlanTab
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.UnscheduledRecipeAdapter
import com.mak.feastit.domain.model.MealPlanRecipe
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
            onMenuClick = { recipe ->
                openBottomSheetFor(recipe)
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

    private val recipeOptions = listOf("Download Recipe", "Remove from meal plan", "Schedule", "Edit Meal time")

    private fun openBottomSheetFor(recipe: MealPlanRecipe) {
        viewModel.openBottomSheet(recipe)
//        (requireParentFragment() as? MealPlannerFragment)?.openBottomSheetFor(MealPlanTab.UnscheduledRecipes, recipeId)
    }

}
