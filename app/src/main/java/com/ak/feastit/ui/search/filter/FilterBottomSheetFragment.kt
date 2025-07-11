// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ak.feastit.R
import com.ak.feastit.databinding.FragmentSearchFilterBottomSheetBinding
import com.ak.feastit.ui.search.FilterGroupItem
import com.ak.feastit.ui.search.FilterType
import com.ak.feastit.ui.search.FilterType.CUISINE
import com.ak.feastit.ui.search.FilterType.DIET
import com.ak.feastit.ui.search.FilterType.INTOLERANCES
import com.ak.feastit.ui.search.FilterType.MEAL
import com.ak.feastit.ui.search.FilterType.SORT
import com.ak.feastit.ui.search.SearchFilter
import com.ak.feastit.ui.search.SearchViewModel
import com.ak.feastit.ui.search.SortBy
import com.ak.feastit.utils.onClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

  private var baseBinding: FragmentSearchFilterBottomSheetBinding? = null
  private val binding: FragmentSearchFilterBottomSheetBinding
    get() = baseBinding!!
  private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    baseBinding = FragmentSearchFilterBottomSheetBinding.inflate(inflater)
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
          renderFilters(filters)
        }
      }
    }
  }

  private fun renderFilters(filters: List<SearchFilter>) {
    binding.sortGroup.removeAllViews()
    binding.cuisineGroup.removeAllViews()
    binding.mealGroup.removeAllViews()
    binding.dietGroup.removeAllViews()
    binding.intoleranceGroup.removeAllViews()
    filters.forEach { filter ->
      when (filter.type) {
        // type casting as the filters are of Single select type only
        SORT -> renderSortFilter(filter as SearchFilter.SingleSelectFilter)
        CUISINE -> renderCuisineFilter(filter as SearchFilter.SingleSelectFilter)
        MEAL -> renderMealFilter(filter as SearchFilter.SingleSelectFilter)
        DIET -> renderDietFilter(filter as SearchFilter.SingleSelectFilter)
        INTOLERANCES -> renderIntoleranceFilter(filter as SearchFilter.SingleSelectFilter)
      }
    }
  }

  private fun renderIntoleranceFilter(filter: SearchFilter.SingleSelectFilter) {
    initChipGroup(binding.intoleranceGroup, filter.options)
  }

  private fun renderDietFilter(filter: SearchFilter.SingleSelectFilter) {
    initChipGroup(binding.dietGroup, filter.options)
  }

  private fun renderMealFilter(filter: SearchFilter.SingleSelectFilter) {
    initChipGroup(binding.mealGroup, filter.options)
  }

  private fun renderCuisineFilter(filter: SearchFilter.SingleSelectFilter) {
    initChipGroup(binding.cuisineGroup, filter.options)
  }

  private fun renderSortFilter(filter: SearchFilter.SingleSelectFilter) {
    initChipGroup(binding.sortGroup, filter.options)
  }

  private fun initChipGroup(
    chipGroup: ChipGroup,
    options: List<FilterGroupItem>,
    showMenu: Boolean = false,
  ) {
    chipGroup.removeAllViews()
    val viewAllChip = Chip(chipGroup.context)
    viewAllChip.text = getString(R.string.view_all)
    viewAllChip.chipIconTint = viewAllChip.textColors
    viewAllChip.isChipIconVisible = true
    viewAllChip.isCheckable = showMenu
    viewAllChip.setChipIconResource(R.drawable.icon_see_all)
    chipGroup.addView(viewAllChip)

    val menu: PopupMenu?
    if (showMenu) {
      menu = PopupMenu(viewAllChip.context, viewAllChip)
      viewAllChip.onClick { menu.show() }
    } else {
      menu = null
      viewAllChip.onClick {
        chipGroup.isSingleLine = !chipGroup.isSingleLine
        viewAllChip.isChecked = !chipGroup.isSingleLine
        chipGroup.requestLayout()
      }
    }
    val singleSelection = true
    options.forEachIndexed { index, option ->
      val chip =
        (layoutInflater.inflate(R.layout.component_filter_chip, chipGroup, false) as Chip)
      chip.id = option.hashCode()
      when (option) {
        is FilterGroupItem.SingleSelectionGroup -> {
          chip.tag = option.name
          chip.text = option.name
          chip.isChecked = option.isSelected
        }

        is FilterGroupItem.SortingGroup -> {
          chip.tag = option.id
          chip.text = option.id
          chip.isChecked = option.isAscending == true
        }
      }
      chipGroup.addView(chip)
      menu?.let { chipMenu ->
//                chipMenu.menu.add(Menu.NONE, index, index, option)
//                chip.isCloseIconVisible = false
      } ?: run {
//                chip.isCloseIconVisible = true
//                chip.setOnCloseIconClickListener { chipGroup.removeView(chip) }
      }
      chip.setOnCheckedChangeListener { _, isChecked ->
        viewModel.onFilterChanged(option, isChecked)
      }
    }
  }

  override fun onDestroyView() {
    baseBinding = null
    super.onDestroyView()
  }

  companion object {
    fun show(fragmentManager: FragmentManager) {
      FilterBottomSheetFragment().show(
        fragmentManager,
        FilterBottomSheetFragment::class.java.name,
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
