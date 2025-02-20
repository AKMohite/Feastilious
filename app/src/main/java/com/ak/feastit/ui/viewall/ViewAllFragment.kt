package com.ak.feastit.ui.viewall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentViewAllBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
internal class ViewAllFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentViewAllBinding.inflate(inflater)

    private val binding: FragmentViewAllBinding
        get() = baseBinding as FragmentViewAllBinding

    private var adapter: ViewAllAdapter? = null

    private val viewModel: ViewAllViewmodel by viewModels()

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        val category = viewModel.getPageTitle()
        binding.categoryTypeTxt.text = category
        adapter = ViewAllAdapter(onRecipeClick = { recipeId ->

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

    /**
     * Trying to create bento box with grids expanding widths depending on [gridColumnCount]
     * @param[position] index of item in list
     * @param[isTablet] is device a tablet
     * @param[gridColumnCount] number of columns in grid
     */
    private fun getGridSpanSize(
        position: Int,
        isTablet: Boolean,
        gridColumnCount: Int
    ): Int {
        return if (isTablet) {
//            TODO maybe for tablet it can be staggered grid like this: https://stackoverflow.com/a/65511718
            /**
             * here 4 is [gridColumnCount] for tablet
             * With span size for mobile it will be as below:
             * |     2     | |     2     |
             * |     2     | |  1 | |  1 |
             * |  1 | |  1 | |     2     |
             * |     2     | |     2     |
             */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
            val result = position % 10
            when(result) {
                0 ,1 , 4, 5, 6, 7 -> 2
                else -> 1 // position = 2, 3, 8, 9
            }
        } else {
            /**
             * here 3 is [gridColumnCount] for mobile
             * With span size for mobile it will be as below:
             * |      3      |
             * |   2   | | 1 |
             */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
            val result = position % 10
            when(result) {
                0, 5 -> gridColumnCount
                1, 4, 7, 8 -> 2
                else -> 1 // result = 2, 3, 6, 9
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
