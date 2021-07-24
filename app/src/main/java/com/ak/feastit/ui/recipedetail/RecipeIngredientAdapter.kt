package com.ak.feastit.ui.recipedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.RecipeIngredItemBinding
import com.ak.feastit.domain.model.Ingredient

class RecipeIngredientAdapter : ListAdapter<Ingredient, RecipeIngredientAdapter.IngredientViewHolder>(IngredientComparator()) {

    class IngredientViewHolder(private val binding: RecipeIngredItemBinding): RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = RecipeIngredItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(
                binding
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient =getItem(position)
        if (ingredient != null)
            holder.bindData(ingredient)
    }
}