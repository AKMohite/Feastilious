package com.ak.feastit.ui.recipes

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.R
import com.ak.feastit.databinding.FragmentRecipeDashboardBinding
import com.ak.feastit.domain.recipelist.Recipe
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class RecipeDashboardFragment : Fragment(R.layout.fragment_recipe_dashboard) {

    private var recipeAdapter: RecipeAdapter? = null
    private val viewModel: RecipeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFlowObservers(view)
    }

    private fun setFlowObservers(view: View) {

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

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.allCategories.collect { categories ->

                categories.forEach { category ->
                    val chip = Chip(context)
                    val paddingDp = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10f,
                            resources.displayMetrics
                    ).toInt()
                    chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
                    chip.text = category.categoryName
                    chip.setOnCheckedChangeListener { selectChip, isChecked ->
//                        TODO add category select search query functionality
                        selectChip.text
                    }

                    binding.categoryChips.addView(chip)
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
        findNavController().navigate(RecipeDashboardFragmentDirections.recipeToRecipeDetail(
                recipeId = recipe.id
        ))
    }
}