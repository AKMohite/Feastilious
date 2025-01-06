package com.ak.feastit.ui.legacyfavorites

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.R
import com.ak.feastit.databinding.LegacyFavoriteRecipesFragmentBinding
import com.ak.feastit.ui.legacyrecipes.LegacyRecipeAdapter
import com.mak.feastit.domain.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegacyFavoriteRecipesFragment : Fragment(R.layout.legacy_favorite_recipes_fragment) {

    private val viewModel: LegacyFavoriteRecipesViewModel by viewModels()
    private var recipeAdapter: LegacyRecipeAdapter = LegacyRecipeAdapter { recipe ->
        navigateToDetails(recipe)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFlowObservers(view)
    }

    private fun setFlowObservers(view: View) {
        val binding =  LegacyFavoriteRecipesFragmentBinding.bind(view)

        binding.apply {
            favRecipeRv.apply {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            favRecipeSearch.feastSearchEt.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.text.trim().isNotBlank())
                        viewModel.favSearchRecipe(v.text.trim().toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favRecipes.collect { recipes ->
                recipeAdapter.submitList(recipes)
            }
        }
    }

    private fun navigateToDetails(recipe: Recipe) {
//        findNavController().navigate(FavoriteRecipesFragmentDirections.favRecipesToRecipeDetail(
//                recipeId = recipe.id
//        ))
    }

}