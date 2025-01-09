package com.ak.feastit.ui.yumdetail.components.instructions.component

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.StepSection
import com.mak.feastit.domain.model.Instruction

internal class InstructionDiffUtil: DiffUtil.ItemCallback<StepSection>() {
    override fun areItemsTheSame(oldItem: StepSection, newItem: StepSection): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: StepSection, newItem: StepSection): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

}
