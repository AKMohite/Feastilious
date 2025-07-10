package com.ak.feastit.ui.mealplanner.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentMealPlanMenuBinding
import com.ak.feastit.ui.mealplanner.MealPlanRecipeSheetItem
import com.ak.feastit.utils.onClick

internal class MealPlanSheetMenuAdapter(
    private val onMenuClick: (item: MealPlanRecipeSheetItem) -> Unit
): RecyclerView.Adapter<MealPlanSheetMenuAdapter.MealPlanSheetMenu>() {

    private val asyncDiff = AsyncListDiffer(this, MealPlanMenuDiff())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealPlanSheetMenu {
        val binding = ComponentMealPlanMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealPlanSheetMenu(binding, onMenuClick)
    }

    override fun onBindViewHolder(
        holder: MealPlanSheetMenu,
        position: Int
    ) {
        val item = asyncDiff.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    fun reload(list: List<MealPlanRecipeSheetItem>) {
        asyncDiff.submitList(list)
    }


    class MealPlanSheetMenu(
        private val binding: ComponentMealPlanMenuBinding,
        private val onMenuClick: (item: MealPlanRecipeSheetItem) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MealPlanRecipeSheetItem) {
            binding.root.apply {
                text = context.getString(item.title)
                setCompoundDrawablesRelativeWithIntrinsicBounds(item.icon, 0, 0, 0)
                onClick { onMenuClick(item) }
            }
        }

    }

}