// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.viewall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentViewAllBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ViewAllFragment : BaseFragment() {

  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentViewAllBinding.inflate(inflater)

  private val binding: FragmentViewAllBinding
    get() = baseBinding as FragmentViewAllBinding

  private var adapter: ViewAllAdapter? = null

  private val viewModel: ViewAllViewmodel by viewModels()

  override fun onViewReady(view: View, savedInstanceState: Bundle?) {
    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }
    val category = viewModel.getPageTitle()
    binding.categoryTypeTxt.text = category
    adapter = ViewAllAdapter(onRecipeClick = { sharedElements, recipeId ->
      gotoDetails(sharedElements, recipeId)
    })
//        binding.viewAllItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    val gridColumnCount = requireContext().resources.getInteger(R.integer.search_grid_column_count)
    val gridLayoutManager = GridLayoutManager(requireContext(), gridColumnCount)
    binding.viewAllItems.layoutManager = gridLayoutManager
    val isTablet = requireContext().resources.getBoolean(R.bool.is_tablet)
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return getGridSpanSize(position, isTablet, gridColumnCount)
      }
    }
    binding.viewAllItems.adapter = adapter
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.state.collectLatest { state ->
//                        adapter?.submitData(state.recipes)
          }
        }
        launch {
          viewModel.observePagedRecipes().collectLatest { pagingData ->
            adapter?.submitData(pagingData)
          }
        }
      }
    }
  }

  private fun gotoDetails(sharedElements: Map<View, String>, recipeId: Long) {
        /*exitTransition = MaterialElevationScale(false).apply {
            duration =
                resources.getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
                    .toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration =
                resources.getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
                    .toLong()
        }*/
    val extras = FragmentNavigatorExtras(
      *sharedElements.toList().toTypedArray(),
    )
    findNavController().navigate(
      directions = ViewAllFragmentDirections.viewAllToRecipeDetail(
        recipeId,
      ),
      navigatorExtras = extras,
    )
  }

  /**
   * Trying to create bento box with grids expanding widths depending on [gridColumnCount]
   * @param[position] index of item in list
   * @param[isTablet] is device a tablet
   * @param[gridColumnCount] number of columns in grid
   */
  private fun getGridSpanSize(
    position: Int,
    isTablet: Boolean,
    gridColumnCount: Int,
  ): Int {
    return if (isTablet) {
//            TODO maybe for tablet it can be staggered grid like this: https://stackoverflow.com/a/65511718
      /**
       * here 12 is [gridColumnCount] for tablet
       * With span size for tablet it will be as below:
       * |         7         | |    5     |
       * |   3  | |  3  | |       6       |
       * |    5     | |         7         |
       * |       6       | |   3  | |  3  |
       */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
      val result = position % 10
      when (result) {
        0, 6 -> 7
        1, 5 -> 5
        2, 3, 8, 9 -> 3
        else -> 6 // position = 4, 7
      }
            /*when(result) {
                0 ,1 , 4, 5, 6, 7 -> 2
                else -> 1 // position = 2, 3, 8, 9
            }*/
    } else {
      /**
       * here 5 is [gridColumnCount] for mobile
       * With span size for mobile it will be as below:
       * |      5      |
       * |   3   | | 2 |
       */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
      val result = position % 10
      when (result) {
        0, 5 -> gridColumnCount
        1, 4, 7, 8 -> 3
        else -> 2 // result = 2, 3, 6, 9
      }

//            This also works but we have different layout
            /*val result = position % 6
            when {
                (result == 0 || result == 3) -> gridColumnCount
                (result == 1 || result == 5) -> 2
                else -> 1
            }*/
    }
  }

  override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
  }
}
