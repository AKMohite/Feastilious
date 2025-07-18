// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import coil3.load
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentYumDetailBinding
import com.ak.feastit.ui.yumdetail.tabs.DetailPagerAdapter
import com.ak.feastit.ui.yumdetail.tabs.RecipeDetailTab
import com.ak.feastit.utils.onClick
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import com.mak.feastit.domain.model.RecipeDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class YumDetailFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentYumDetailBinding.inflate(inflater)

  private val binding: FragmentYumDetailBinding
    get() = baseBinding as FragmentYumDetailBinding

  private val viewModel: YumDetailViewModel by viewModels()

  private val tabLayoutMediator: TabLayoutMediator by lazy {
    TabLayoutMediator(
      binding.recipeDetailTabs,
      binding.recipeDetailPager,
    ) { tab, position ->
      tab.text =
        when (position) {
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

  private val offsetChangeListener =
    AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
      if (abs(verticalOffset) >= binding.appBar.totalScrollRange) {
        binding.collapsingToolbar.title = recipeName ?: ""
      } else {
        binding.collapsingToolbar.title = ""
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val transition =
      MaterialContainerTransform().apply {
        drawingViewId = R.id.nav_host_fragment
        duration =
          resources
            .getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
            .toLong()
//            scrimColor = Color.TRANSPARENT
//            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
      }
    sharedElementEnterTransition = transition
    sharedElementReturnTransition = transition
  }

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
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
    binding.recipeImg.transitionName =
      getString(R.string.recipe_to_detail_image, arguments?.getLong(SAVED_RECIPE_ID))
//        binding.recipeName.transitionName = getString(R.string.recipe_to_detail_name, arguments?.getLong(SAVED_RECIPE_ID))
    postponeEnterTransition()
    view?.doOnPreDraw { startPostponedEnterTransition() }
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
    val favIcon =
      if (recipe.isAddedToCollection) {
        R.drawable.ic_favorite_filled
      } else {
        R.drawable.ic_favorite_border
      }
    binding.addToFavBtn.icon = ContextCompat.getDrawable(requireContext(), favIcon)
  }

  override fun onDestroyView() {
    binding.appBar.removeOnOffsetChangedListener(offsetChangeListener)
    tabLayoutMediator.detach()
    super.onDestroyView()
  }
}
