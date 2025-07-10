package com.ak.feastit.ui.yumdetail.tabs.instructions.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentHeaderButtonBinding
import com.ak.feastit.databinding.ComponentRecipeStepBinding
import com.ak.feastit.ui.yumdetail.StepSection
import com.ak.feastit.ui.yumdetail.tabs.ingredients.component.ComponentHeaderButton

private const val STEP_HEADER = 0
private const val STEP_ITEM = 1

internal class RecipeInstructionsAdapter(
    private val onToggleMealPlanner: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncDiffUtil = AsyncListDiffer(this, InstructionDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            STEP_HEADER -> {
                val binding = ComponentHeaderButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComponentHeaderButton(binding, onToggleMealPlanner)
            }
            STEP_ITEM -> {
                val binding = ComponentRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ComponentInstructionStep(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun getItemCount(): Int = asyncDiffUtil.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = asyncDiffUtil.currentList[position]
        when(holder.itemViewType) {
            STEP_HEADER -> {
                val section = item as StepSection.Header
                (holder as ComponentHeaderButton).bind(section.title)
            }
            STEP_ITEM -> {
                val section = item as StepSection.Item
                (holder as ComponentInstructionStep).bind(section.instruction)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (asyncDiffUtil.currentList[position]) {
            is StepSection.Header -> STEP_HEADER
            is StepSection.Item -> STEP_ITEM
        }
    }

    fun reload(list: List<StepSection>) {
        with(asyncDiffUtil) {
            submitList(list)
        }
    }

}
