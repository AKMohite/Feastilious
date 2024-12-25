package com.ak.feastit.ui.explore.experimental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ExploreRecipeItemBinding
import com.ak.feastit.ui.explore.components.ExploreRecipeViewHolder
import com.mak.feastit.domain.model.Recipe

internal class ExploreRecipeAdapter(
    private val items: List<Recipe>,
    private val sectionEvents: ((ExploreItemAction) -> Unit)? = null
) : RecyclerView.Adapter<ExploreRecipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreRecipeViewHolder {
        val binding = ExploreRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreRecipeViewHolder(binding, sectionEvents)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ExploreRecipeViewHolder, position: Int) {
        holder.bind(items[position])
//        holder.itemView.onClick { sectionEvents(ExploreItemAction.RecipeClick(items[position].id)) }
    }

}
