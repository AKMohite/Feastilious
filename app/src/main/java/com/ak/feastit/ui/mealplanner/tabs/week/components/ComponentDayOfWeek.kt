package com.ak.feastit.ui.mealplanner.tabs.week.components

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentDayOfWeekBinding
import com.ak.feastit.utils.show
import com.mak.feastit.domain.model.DayMealPlan

class ComponentDayOfWeek(
    private val binding: ComponentDayOfWeekBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(day: DayMealPlan) {
        binding.dayTv.text = day.day
        val toggleIcon = if (day.isExpanded) {
            R.drawable.ic_down
        } else {
            R.drawable.ic_previous
        }
        binding.toggleIcon.apply {
            icon = ContextCompat.getDrawable(context, toggleIcon)
            text = day.noOfMeal
            show(day.noOfMeal.isNotBlank())
        }
    }

}