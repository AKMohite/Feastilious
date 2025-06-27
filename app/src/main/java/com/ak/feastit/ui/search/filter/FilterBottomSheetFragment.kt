package com.ak.feastit.ui.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ak.feastit.R
import com.ak.feastit.databinding.FragmentSearchFilterBottomSheetBinding
import com.ak.feastit.ui.search.FilterType
import com.ak.feastit.ui.search.FilterType.CUISINE
import com.ak.feastit.ui.search.FilterType.DIET
import com.ak.feastit.ui.search.FilterType.INTOLERANCES
import com.ak.feastit.ui.search.FilterType.MEAL
import com.ak.feastit.ui.search.FilterType.SORT
import com.ak.feastit.ui.search.SearchViewModel
import com.ak.feastit.ui.search.SortBy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSearchFilterBottomSheetBinding? = null
    private val binding: FragmentSearchFilterBottomSheetBinding
        get() = _binding!!
    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFilterBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observers()
    }

    private fun setupView() {
        viewModel.loadFilters()
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filters.collectLatest { filters ->
                    if (filters.isEmpty()) return@collectLatest
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            FilterBottomSheetFragment().show(
                fragmentManager,
                FilterBottomSheetFragment::class.java.name
            )
        }
    }
}


@StringRes
internal fun FilterType.getTitle(): Int {
    return when (this) {
        SORT -> R.string.filter_sort
        CUISINE -> R.string.filter_cuisine
        MEAL -> R.string.filter_meal
        DIET -> R.string.filter_diet
        INTOLERANCES -> R.string.filter_intolerance
    }
}

@StringRes
internal fun SortBy.getTitle(): Int {
    return when (this) {
        SortBy.POPULAR -> R.string.filter_sort_popular
        SortBy.TOP_RATED -> R.string.filter_sort_top_rated
        SortBy.HEALTHY -> R.string.filter_sort_healthy
        SortBy.QUICK -> R.string.filter_sort_quick
        SortBy.POCKET_FRIENDLY -> R.string.filter_sort_pocket_friendly
    }
}