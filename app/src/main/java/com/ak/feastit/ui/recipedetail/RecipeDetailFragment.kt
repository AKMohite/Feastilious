package com.ak.feastit.ui.recipedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ak.feastit.R
import com.ak.feastit.databinding.RecipeDetailFragmentBinding
import com.ak.feastit.domain.recipelist.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment(R.layout.recipe_detail_fragment) {

    private val viewModel: RecipeDetailViewModel by viewModels()
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = RecipeDetailFragmentBinding.bind(view)

        viewModel.getRecipe(Recipe(id = args.recipeId.toLong(), recipeName = args.recipeName, recipeImgUrl = args.recipeImage))
    }

}