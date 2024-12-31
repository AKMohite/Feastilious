package com.ak.feastit.ui.legacyrecipedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeStepBinding
import com.mak.feastit.domain.model.Instruction

class LegacyRecipeInstructionAdapter : ListAdapter<Instruction, LegacyRecipeInstructionAdapter.LegacyInstructionViewHolder>(LegacyInstructionComparator()) {

    class LegacyInstructionViewHolder(private val binding: ComponentRecipeStepBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(instruction: Instruction) {
            binding.tvStepNo.text = instruction.stepNo
            binding.tvStepDesc.text = instruction.stepDesc
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegacyInstructionViewHolder {
        val binding = ComponentRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LegacyInstructionViewHolder(
                binding
        )
    }

    override fun onBindViewHolder(holder: LegacyInstructionViewHolder, position: Int) {
        val ingredient = getItem(position)
        if (ingredient != null)
            holder.bindData(ingredient)
    }
}