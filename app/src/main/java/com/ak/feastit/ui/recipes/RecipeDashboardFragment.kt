package com.ak.feastit.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.R
import com.ak.feastit.databinding.FragmentRecipeDashboardBinding
import com.ak.feastit.domain.recipelist.Recipe
import com.ak.feastit.utils.APP_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipeDashboardFragment : Fragment(R.layout.fragment_recipe_dashboard) {

    private var recipeAdapter: RecipeAdapter? = null
    private val viewModel: RecipeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  FragmentRecipeDashboardBinding.bind(view)
        recipeAdapter = RecipeAdapter { recipe ->
            navigateToDetails(recipe)
        }
        binding.apply {
            recipeRv.apply {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        setFlowObservers()
    }

    private fun setFlowObservers() {

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.allCategories.collect { categories ->
                categories.forEach {category ->
                    Log.d(APP_TAG, "onActivityCreated: ${category.categoryName}")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dashboardRecipes.collect { recipes ->
                recipeAdapter?.submitList(recipes) // TODO check callbacks
            }
        }
    }

    private fun navigateToDetails(recipe: Recipe) {
        TODO("Not yet implemented")
    }
}