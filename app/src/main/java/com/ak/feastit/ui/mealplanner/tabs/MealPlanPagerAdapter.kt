package com.ak.feastit.ui.mealplanner.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ak.feastit.ui.mealplanner.tabs.today.MealPlanTodayFragment
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.UnscheduledRecipeFragment
import com.ak.feastit.ui.mealplanner.tabs.week.MealPlanWeekFragment

internal enum class MealPlanTab {
    Today,
    Week,
    UnscheduledRecipes
}

internal class MealPlanPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = MealPlanTab.entries.count()

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            MealPlanTab.Today.ordinal -> {
                MealPlanTodayFragment()
            }
            MealPlanTab.Week.ordinal -> {
                MealPlanWeekFragment()
            }
            MealPlanTab.UnscheduledRecipes.ordinal -> {
                UnscheduledRecipeFragment()
            }
            else -> throw IllegalArgumentException("Invalid tab position: $position")
        }
    }
}