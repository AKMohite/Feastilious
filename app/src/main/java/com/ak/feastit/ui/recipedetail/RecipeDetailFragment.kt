package com.ak.feastit.ui.recipedetail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.RecipeDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipeDetailFragment : Fragment(R.layout.recipe_detail_fragment) {

    private val viewModel: RecipeDetailViewModel by viewModels()
//    private val args: RecipeDetailFragmentArgs by navArgs()
    private val ingredsAdapter = RecipeIngredientAdapter()
    private val instsAdapter = RecipeInstructionAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = RecipeDetailFragmentBinding.bind(view)

//        viewModel.getRecipe(args.recipeId)

        val headingAdapter = RecipeHeadingAdapter(getString(R.string.summary))

        binding.apply {
            recipeDetailsRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ConcatAdapter(
                    headingAdapter,
                    RecipeHeadingAdapter(getString(R.string.ingredients)),
                    ingredsAdapter,
                    RecipeHeadingAdapter(getString(R.string.instructions)),
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