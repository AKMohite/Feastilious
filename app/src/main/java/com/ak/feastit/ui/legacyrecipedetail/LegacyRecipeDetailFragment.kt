package com.ak.feastit.ui.legacyrecipedetail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.LegacyRecipeDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegacyRecipeDetailFragment : Fragment(R.layout.legacy_recipe_detail_fragment) {

    private val viewModel: LegacyRecipeDetailViewModel by viewModels()
//    private val args: RecipeDetailFragmentArgs by navArgs()
    private val ingredsAdapter = LegacyRecipeIngredientAdapter()
    private val instsAdapter = LegacyRecipeInstructionAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = LegacyRecipeDetailFragmentBinding.bind(view)

//        viewModel.getRecipe(args.recipeId)

        val headingAdapter = LegacyRecipeHeadingAdapter(getString(R.string.summary))

        binding.apply {
            recipeDetailsRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ConcatAdapter(
                    headingAdapter,
                    LegacyRecipeHeadingAdapter(getString(R.string.ingredients)),
                    ingredsAdapter,
                    LegacyRecipeHeadingAdapter(getString(R.string.instructions)),
                    instsAdapter

                )
            }

            recipeFavBtn.setOnClickListener {
                viewModel.toggleFav()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.recipeDetail.collect { recipeDetail ->
                recipeDetail?.let { detail ->
                    binding.recipeImg.load(detail.recipeImg) {
                        placeholder(R.drawable.ic_recipe_img_placeholder)
                        error(R.drawable.ic_recipe_img_placeholder)
                    }
                    binding.recipeName.text = detail.recipeName
                    headingAdapter.subtitle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(detail.recipeSummary, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(detail.recipeSummary)
                    ingredsAdapter.submitList(detail.ingredients)
                    instsAdapter.submitList(detail.instructions)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.favRecipe.collect { fav ->
                if (fav)
                    binding.recipeFavBtn.setImageResource(R.drawable.ic_favorite_filled)
                else
                    binding.recipeFavBtn.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }

}