package com.ak.feastit.ui.yumdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import coil.load
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentYumDetailBinding
import com.ak.feastit.ui.yumdetail.tabs.DetailPagerAdapter
import com.ak.feastit.ui.yumdetail.tabs.RecipeDetailTab
import com.ak.feastit.utils.onClick
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mak.feastit.domain.model.RecipeDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs

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

    private var recipeName: String? = null

    private val offsetChangeListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        if (abs(verticalOffset) >= binding.appBar.totalScrollRange) {
            binding.collapsingToolbar.title = recipeName ?: ""
        } else {
            binding.collapsingToolbar.title = ""
        }
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun observers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state
                        .filter { state -> state.overview != null }
                        .collectLatest { state ->
                            val recipe = state.overview!!
                            renderView(recipe)
                        }
                }
            }
        }
    }

    private fun setupView() {
        binding.recipeDetailPager.isUserInputEnabled = false
        binding.recipeDetailPager.adapter = detailPagerAdapter
        binding.appBar.addOnOffsetChangedListener(offsetChangeListener)
        tabLayoutMediator.attach()
        binding.addToFavBtn.onClick {
            viewModel.toggleFavorite()
        }
    }

    private fun renderView(recipe: RecipeDetail) {
        recipeName = recipe.recipeName
        binding.recipeName.text = recipeName
        binding.recipeImg.load(recipe.recipeImg)
        val favIcon = if (recipe.isAddedToCollection)
            R.drawable.ic_favorite_filled
        else R.drawable.ic_favorite_border
        binding.addToFavBtn.icon = ContextCompat.getDrawable(requireContext(), favIcon)
    }

    override fun onDestroyView() {
        binding.appBar.removeOnOffsetChangedListener(offsetChangeListener)
        tabLayoutMediator.detach()
        super.onDestroyView()
    }

}