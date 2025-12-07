package com.ak.feastit.ui.yumdetail.tabs.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentNutrientBinding
import com.mak.feastit.domain.model.Nutrient

internal class NutrientAdapter : ListAdapter<Nutrient, NutrientViewHolder>(Diff) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ComponentNutrientBinding.inflate(inflater, parent, false)
    return NutrientViewHolder(binding)
  }

  override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  private object Diff : DiffUtil.ItemCallback<Nutrient>() {
    override fun areItemsTheSame(oldItem: Nutrient, newItem: Nutrient): Boolean = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Nutrient, newItem: Nutrient): Boolean = oldItem == newItem
  }
}

internal class NutrientViewHolder(
  private val binding: ComponentNutrientBinding,
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(item: Nutrient) {
    binding.nutrientName.text = item.name
    binding.nutrientAmount.text = "${item.amount} ${item.unit}"
    binding.nutrientDailyPercent.text = "${item.dailyPercent}%"
  }
}
