package com.ak.feastit.ui.yumdetail.tabs.instructions.component

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeStepBinding
import com.mak.feastit.domain.model.Instruction

internal class ComponentInstructionStep(
    private val binding: ComponentRecipeStepBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(instruction: Instruction) {
        binding.tvStepNo.text = instruction.stepNo
        binding.tvStepDesc.text = instruction.stepDesc
    }
}
