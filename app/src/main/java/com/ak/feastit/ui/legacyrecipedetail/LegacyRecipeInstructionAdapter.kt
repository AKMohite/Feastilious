package com.ak.feastit.ui.legacyrecipedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.RecipeStepItemBinding
import com.mak.feastit.domain.model.Instruction

class LegacyRecipeInstructionAdapter : ListAdapter<Instruction, LegacyRecipeInstructionAdapter.InstructionViewHolder>(LegacyInstructionComparator()) {

    class InstructionViewHolder(private val binding: RecipeStepItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(instruction: Instruction) {
            binding.tvStepNo.text = instruction.stepNo
            binding.tvStepDesc.text = instruction.stepDesc
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val binding = RecipeStepItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstructionViewHolder(
                binding
        )
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val ingredient = getItem(position)
        if (ingredient != null)
            holder.bindData(ingredient)
    }
}