// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ak.feastit.ui.yumdetail.tabs.ingredients.RecipeIngredientsFragment
import com.ak.feastit.ui.yumdetail.tabs.instructions.RecipeInstructionsFragment
import com.ak.feastit.ui.yumdetail.tabs.nutrition.RecipeNutritionFragment
import com.ak.feastit.ui.yumdetail.tabs.overview.RecipeOverviewFragment

internal enum class RecipeDetailTab {
  Overview,
  Ingredients,
  Instructions,
  Nutrition,
}

internal class DetailPagerAdapter(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
) : FragmentStateAdapter(
  fragmentManager,
  lifecycle,
) {
  override fun getItemCount(): Int = RecipeDetailTab.entries.size

  override fun createFragment(position: Int): Fragment = when (position) {
    RecipeDetailTab.Overview.ordinal -> {
      RecipeOverviewFragment()
    }

    RecipeDetailTab.Ingredients.ordinal -> {
      RecipeIngredientsFragment()
    }

    RecipeDetailTab.Instructions.ordinal -> {
      RecipeInstructionsFragment()
    }

    RecipeDetailTab.Nutrition.ordinal -> {
      RecipeNutritionFragment()
    }

    else -> throw IllegalArgumentException("Invalid tab position: $position")
  }
}
