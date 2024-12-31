package com.ak.feastit.ui.legacyrecipedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentRecipeIngredientBinding
import com.mak.feastit.domain.model.Ingredient

class LegacyRecipeIngredientAdapter : ListAdapter<Ingredient, LegacyRecipeIngredientAdapter.LegacyIngredientViewHolder>(LegacyIngredientComparator()) {

    class LegacyIngredientViewHolder(private val binding: ComponentRecipeIngredientBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(ingredient: Ingredient) {

            binding.apply {
                imgIngredient.load(ingredient.image) {
                    error(R.drawable.ic_recipe_img_placeholder)
                    placeholder(R.drawable.ic_recipe_img_placeholder)
                }

                tvIngredientDesc.text = ingredient.name
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegacyIngredientViewHolder {
        val binding = ComponentRecipeIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LegacyIngredientViewHolder(
                binding
        )
    }

    override fun onBindViewHolder(holder: LegacyIngredientViewHolder, position: Int) {
        val ingredient =getItem(position)
        if (ingredient != null)
            holder.bindData(ingredient)
    }
}