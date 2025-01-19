package com.ak.feastit.ui.mealplanner.tabs.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentDayOfWeekBinding
import com.ak.feastit.databinding.ComponentMealPlanRecipeBinding
import com.ak.feastit.ui.mealplanner.WeekMealPlanSection
import com.ak.feastit.ui.mealplanner.tabs.unscheduled.components.ComponentMealPlanRecipe
import com.ak.feastit.ui.mealplanner.tabs.week.components.ComponentDayOfWeek
import com.ak.feastit.utils.show

private const val WEEK_HEADER = 0
private const val WEEK_MEAL = 1

internal class MealPlanWeekAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncDiff = AsyncListDiffer(this, MealPlanWeekDiff())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            WEEK_HEADER ->{
                val binding = ComponentDayOfWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComponentDayOfWeek(binding)
            }
            WEEK_MEAL ->{
                val binding = ComponentMealPlanRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComponentMealPlanRecipe(binding)
            }
            else -> throw IllegalStateException("Invalid view type $viewType rendering")
        }
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = asyncDiff.currentList[position]
        when(holder.itemViewType) {
            WEEK_HEADER -> {
                val item = section as WeekMealPlanSection.DayHeader
                (holder as ComponentDayOfWeek).bind(item.day)
            }
            WEEK_MEAL -> {
                val item = section as WeekMealPlanSection.MealRecipe
                val componentMeal = holder as ComponentMealPlanRecipe
                componentMeal.bind(item.meal)
                componentMeal.itemView.show(item.isExpanded)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(asyncDiff.currentList[position]) {
            is WeekMealPlanSection.DayHeader -> WEEK_HEADER
            is WeekMealPlanSection.MealRecipe -> WEEK_MEAL
        }
    }

    fun reload(sections: List<WeekMealPlanSection>) {
        asyncDiff.submitList(sections)
    }
}