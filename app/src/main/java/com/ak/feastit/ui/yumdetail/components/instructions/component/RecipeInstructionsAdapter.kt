package com.ak.feastit.ui.yumdetail.components.instructions.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeStepBinding
import com.mak.feastit.domain.model.Instruction

internal class RecipeInstructionsAdapter : RecyclerView.Adapter<InstructionViewHolder>() {

    private val asyncDiffUtil = AsyncListDiffer(this, InstructionDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val binding = ComponentRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstructionViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncDiffUtil.currentList.size

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val step = asyncDiffUtil.currentList[position]
        holder.bind(step)
    }

    fun reload(list: List<Instruction>) {
        with(asyncDiffUtil) {
            submitList(list)
        }
    }

}
