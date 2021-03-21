package com.ak.feastit.ui.recipedetail

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.domain.recipedetails.Ingredient
import com.ak.feastit.domain.recipelist.Instruction

class InstructionComparator : DiffUtil.ItemCallback<Instruction>() {

    override fun areItemsTheSame(oldItem: Instruction, newItem: Instruction) =
            oldItem.stepNo == newItem.stepNo

    override fun areContentsTheSame(oldItem: Instruction, newItem: Instruction) =
            oldItem == newItem
}