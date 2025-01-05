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
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentFavoritesBinding
import com.ak.feastit.ui.favorites.components.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class FavoritesFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentFavoritesBinding.inflate(inflater)

    private val binding: FragmentFavoritesBinding
        get() = baseBinding as FragmentFavoritesBinding

    private val viewmodel: FavoriteViewModel by viewModels()

    private val adapter: FavoritesAdapter by lazy {
        FavoritesAdapter(
            onRecipeClick = { recipeId -> navigateToDetails(recipeId)}
        )
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.favoriteRecipes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favoriteRecipes.adapter = adapter
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collectLatest { state ->
                    adapter.reload(state.recipes)
                }
            }
        }
    }

    private fun navigateToDetails(recipeId: Long) {
        findNavController().navigate(FavoritesFragmentDirections.favRecipesToRecipeDetail(recipeId))
    }

}