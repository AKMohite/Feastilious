// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.tabs.today

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
import com.ak.feastit.databinding.FragmentMealPlanTodayBinding
import com.ak.feastit.ui.mealplanner.MealPlannerFragment
import com.ak.feastit.ui.mealplanner.MealPlannerFragmentDirections
import com.ak.feastit.ui.mealplanner.MealPlannerViewModel
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.OnMealPlanClick
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.UnscheduledRecipeAdapter
import com.ak.feastit.utils.show
import kotlinx.coroutines.launch

internal class MealPlanTodayFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding? = FragmentMealPlanTodayBinding.inflate(inflater)

  private val binding: FragmentMealPlanTodayBinding
    get() = baseBinding as FragmentMealPlanTodayBinding

  private val viewmodel: MealPlannerViewModel by viewModels({ requireParentFragment() })

  // TODO: We can have same adapter for all tabs.
  private var adapter: UnscheduledRecipeAdapter? = null

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    setupView()
    observers()
  }

  private fun setupView() {
    binding.emptyState.emptyImg.setImageResource(R.drawable.ic_recipe_img_placeholder)
    binding.emptyState.emptyHeader.text = getString(R.string.today_empty_header)
    binding.emptyState.emptyBody.text = getString(R.string.today_empty_body)
    adapter =
      UnscheduledRecipeAdapter(
        onMealPlanClick = ::handleMealPlanEvent,
      )
    binding.todayMeals.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.todayMeals.adapter = adapter
  }

  private fun observers() {
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewmodel.state.collect { state ->
          binding.emptyState.root.show(state.todayRecipes.isEmpty())
          binding.todayMeals.show(state.todayRecipes.isNotEmpty())
          adapter?.reload(state.todayRecipes)
        }
      }
    }
  }

  private fun handleMealPlanEvent(event: OnMealPlanClick) {
    when (event) {
      is OnMealPlanClick.MoreMenu -> viewmodel.openBottomSheet(event.recipe)
      is OnMealPlanClick.RecipeDetail -> {
        (requireParentFragment() as MealPlannerFragment)
          .findNavController()
          .navigate(MealPlannerFragmentDirections.plannerToRecipeDetail(event.recipe.recipeId))
      }
    }
  }

  override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
  }
}
