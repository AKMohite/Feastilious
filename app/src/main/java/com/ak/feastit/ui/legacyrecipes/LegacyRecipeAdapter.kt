package com.ak.feastit.ui.legacyrecipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ak.feastit.R
import com.ak.feastit.databinding.RecipeItemBinding
import com.mak.feastit.domain.model.Recipe

class LegacyRecipeAdapter(
        private val onItemClick: (Recipe) -> Unit
): ListAdapter<Recipe, LegacyRecipeAdapter.RecipeViewHolder>(LegacyRecipeComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(
                binding,
                onItemClick = { position ->
                    val recipe = getItem(position)
                    if (recipe != null)
                        onItemClick(recipe)
                }
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe =getItem(position)
        if (recipe != null)
            holder.bindData(recipe)
    }

    class RecipeViewHolder(private val binding: RecipeItemBinding, private val onItemClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        onItemClick(position)
                }
            }
        }

        fun bindData(recipe: Recipe) {
            binding.apply {
                recipeImg.load(recipe.recipeImgUrl) {
                    placeholder(R.drawable.ic_recipe_img_placeholder)
                    error(R.drawable.ic_recipe_img_placeholder)
                }

                recipeName.text = recipe.recipeName
            }
        }
    }
}