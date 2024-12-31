package com.ak.feastit.ui.yumdetail.components.instructions.component

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.Instruction

internal class InstructionDiffUtil: DiffUtil.ItemCallback<Instruction>() {
    override fun areItemsTheSame(oldItem: Instruction, newItem: Instruction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Instruction, newItem: Instruction): Boolean {
        return oldItem.stepNo == newItem.stepNo
    }

}
