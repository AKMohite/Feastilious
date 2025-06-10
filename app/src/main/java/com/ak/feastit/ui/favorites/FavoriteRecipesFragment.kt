package com.ak.feastit.ui.favorites

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
import com.ak.feastit.databinding.FragmentFavoritesBinding
import com.ak.feastit.ui.favorites.components.FavoritesAdapter
import com.ak.feastit.ui.favorites.components.SearchSuggestionAdapter
import com.ak.feastit.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class FavoriteRecipesFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentFavoritesBinding.inflate(inflater)

    private val binding: FragmentFavoritesBinding
        get() = baseBinding as FragmentFavoritesBinding

    private val viewmodel: FavoriteViewModel by viewModels()

    private var adapter: FavoritesAdapter? = null

    private val suggestionsAdapter: SearchSuggestionAdapter by lazy {
        SearchSuggestionAdapter(
            onRecipeClick = { recipeId -> navigateToDetails(recipeId)}
        )
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    private fun setupView() {
        binding.emptyState.emptyImg.setImageResource(R.drawable.ic_recipe_img_placeholder)
        binding.emptyState.emptyHeader.text = ""
        binding.emptyState.emptyBody.text = ""
        binding.favoriteRecipes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favoriteRecipes.adapter = adapter
        binding.suggestionItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.suggestionItems.adapter = suggestionsAdapter
        adapter = FavoritesAdapter(
            onRecipeClick = { recipeId -> navigateToDetails(recipeId)}
        )
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewmodel.state.collectLatest { state ->
                        binding.emptyState.root.show(state.recipes.isEmpty())
                        binding.favoriteRecipes.show(state.recipes.isNotEmpty())
                        adapter?.reload(state.recipes)
                        suggestionsAdapter.reload(state.suggestions)
                    }
                }
            }
        }
    }

    private fun navigateToDetails(recipeId: Long) {
        findNavController().navigate(FavoriteRecipesFragmentDirections.favRecipesToRecipeDetail(recipeId))
    }

}