// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabNutritionBinding
import com.ak.feastit.ui.yumdetail.YumDetailViewModel
import com.ak.feastit.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class RecipeNutritionFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentDetailTabNutritionBinding.inflate(inflater)

  private val binding: FragmentDetailTabNutritionBinding
    get() = baseBinding as FragmentDetailTabNutritionBinding

  private val viewModel: YumDetailViewModel by viewModels({ requireParentFragment() })
  private var adapter: NutrientAdapter? = null

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    setupList()
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collectLatest { state ->
          renderPie(state)
          adapter?.submitList(state.nutrients)
        }
      }
    }
  }

  private fun setupList() {
    binding.nutrientsList.layoutManager = LinearLayoutManager(requireContext())
    adapter = NutrientAdapter()
    binding.nutrientsList.adapter = adapter
    binding.nutrientsList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
  }

  private fun renderPie(state: com.ak.feastit.ui.yumdetail.YumDetailState) {
    val breakdown = state.overview?.caloricBreakdown ?: emptyMap()
    val container: FrameLayout = binding.pieChartContainer
    val legend = binding.pieLegendContainer
    legend.removeAllViews()
    container.show(breakdown.isNotEmpty())
    container.removeAllViews()
    if (breakdown.isEmpty()) {
      return
    }
    val view = CaloriePieChartView(requireContext())
    view.setData(breakdown)
    container.addView(
      view,
      FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT,
      ),
    )

    // Legend rows - generate as many distinct colors as needed
    val total = breakdown.values.sum().takeIf { it > 0.0 } ?: 1.0
    breakdown.toList().forEach { (label, value) ->
      val row = LegendRowView(requireContext())
      row.bind(colorInt = CaloriePieChartView.colorFor("$label-$value"), label = label, percent = (value * 100.0 / total))
      legend.addView(row)
    }
  }

  override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
  }
}
