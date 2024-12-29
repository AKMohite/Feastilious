package com.ak.feastit.ui.legacyrecipes

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.R
import com.ak.feastit.databinding.FragmentRecipeDashboardBinding
import com.google.android.material.chip.Chip

class LegacyRecipeDashboardFragment : Fragment(R.layout.legacy_fragment_recipe_dashboard) {

    private var recipeAdapter: LegacyRecipeAdapter? = null
    private val viewModel: LegacyRecipeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFlowObservers(view)
    }

    private fun setFlowObservers(view: View) {

        val binding =  FragmentRecipeDashboardBinding.bind(view)
        recipeAdapter = LegacyRecipeAdapter {  }
        binding.apply {
            recipeRv.apply {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            recipeSearch.feastSearchEt.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!v.text.trim().isBlank())
                        viewModel.searchRecipe(v.text.trim().toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
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
                        if (isChecked) {
                            binding.recipeSearch.feastSearchEt.setText("")
                            viewModel.searchByCategory(selectChip.text.toString())
                        } else {
                            viewModel.searchRecipe("")
                        }
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

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.selectedCategory.collect { category ->
                if (category.isBlank()) {
                    binding.categoryChips.clearCheck()
                }
            }
        }
    }
}