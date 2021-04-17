package com.ak.feastit.ui.favorites

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ak.feastit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.favorite_recipes_fragment) {

    private val viewModel: FavoriteRecipesViewModel by viewModels()

}