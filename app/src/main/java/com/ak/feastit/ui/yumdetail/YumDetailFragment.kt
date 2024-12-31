package com.ak.feastit.ui.yumdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import coil.load
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentYumDetailBinding
import com.ak.feastit.ui.yumdetail.components.DetailPagerAdapter
import com.ak.feastit.ui.yumdetail.components.RecipeDetailTab
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class YumDetailFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding
        = FragmentYumDetailBinding.inflate(inflater)

    private val binding: FragmentYumDetailBinding
        get() = baseBinding as FragmentYumDetailBinding

    private val viewModel: YumDetailViewModel by viewModels()

    private val tabLayoutMediator: TabLayoutMediator by lazy {
        TabLayoutMediator(
            binding.recipeDetailTabs,
            binding.recipeDetailPager
        ) { tab, position ->
            tab.text = when (position) {
                RecipeDetailTab.Overview.ordinal -> getString(R.string.detail_overview)
                RecipeDetailTab.Ingredients.ordinal -> getString(R.string.detail_ingredients)
                RecipeDetailTab.Instructions.ordinal -> getString(R.string.detail_instructions)
                RecipeDetailTab.Nutrition.ordinal -> getString(R.string.detail_nutrition)
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }
    }
    private val detailPagerAdapter: DetailPagerAdapter by lazy {
        DetailPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        binding.recipeDetailPager.adapter = detailPagerAdapter
        tabLayoutMediator.attach()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state
                        .filter { state -> state.overview != null }
                        .collectLatest { state ->
                            val recipe = state.overview!!
                            binding.recipeName.text = recipe.recipeName
//                            binding.detailToolbar.title = recipe.recipeName
                            binding.recipeImg.load(recipe.recipeImg)
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        tabLayoutMediator.detach()
        super.onDestroyView()
    }

}