package com.ak.feastit.ui.legacyrecipedetail

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.Instruction

class LegacyInstructionComparator : DiffUtil.ItemCallback<Instruction>() {

    override fun areItemsTheSame(oldItem: Instruction, newItem: Instruction) =
            oldItem.stepNo == newItem.stepNo

    override fun areContentsTheSame(oldItem: Instruction, newItem: Instruction) =
            oldItem == newItem
}