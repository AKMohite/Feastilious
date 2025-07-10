package com.ak.feastit.ui.mealplanner.tabs.unscheduled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentUnscheduledRecipesBinding
import com.ak.feastit.ui.mealplanner.MealPlannerFragment
import com.ak.feastit.ui.mealplanner.MealPlannerFragmentDirections
import com.ak.feastit.ui.mealplanner.MealPlannerViewModel
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.OnMealPlanClick
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.UnscheduledRecipeAdapter
import com.ak.feastit.utils.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class UnscheduledRecipeFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentUnscheduledRecipesBinding.inflate(inflater)
    }

    private val binding: FragmentUnscheduledRecipesBinding
        get() = baseBinding as FragmentUnscheduledRecipesBinding

    private val viewModel: MealPlannerViewModel by viewModels({ requireParentFragment() })

    private var adapter: UnscheduledRecipeAdapter? = null

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.emptyState.emptyImg.setImageResource(R.drawable.ic_recipe_img_placeholder)
        binding.emptyState.emptyHeader.text = getString(R.string.unschedule_empty_header)
        binding.emptyState.emptyBody.text = getString(R.string.unscheduled_empty_body)
        binding.unscheduledRecipes.layoutManager = LinearLayoutManager(requireContext())
        adapter = UnscheduledRecipeAdapter(
            onMealPlanClick = { onMealPlanClick ->
                onItemClick(onMealPlanClick)
            }
        )
        binding.unscheduledRecipes.adapter = adapter
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.emptyState.root.show(state.unscheduledRecipes.isEmpty())
                    binding.unscheduledRecipes.show(state.unscheduledRecipes.isNotEmpty())
                    adapter?.reload(state.unscheduledRecipes)
                }
            }
        }
    }

    private fun onItemClick(event: OnMealPlanClick) {
        when(event) {
            is OnMealPlanClick.MoreMenu -> {
                viewModel.openBottomSheet(event.recipe)
//        (requireParentFragment() as? MealPlannerFragment)?.openBottomSheetFor(MealPlanTab.UnscheduledRecipes, recipeId)
            }
            is OnMealPlanClick.RecipeDetail -> {
                (requireParentFragment() as MealPlannerFragment)
                    .findNavController().navigate(MealPlannerFragmentDirections.plannerToRecipeDetail(event.recipe.recipeId))
            }
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

}
