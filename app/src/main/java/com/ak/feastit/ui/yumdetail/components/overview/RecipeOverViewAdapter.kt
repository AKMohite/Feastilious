package com.ak.feastit.ui.yumdetail.components.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentTextItemBinding
import com.ak.feastit.ui.yumdetail.components.overview.component.OverviewTextViewHolder
import com.ak.feastit.ui.yumdetail.components.overview.component.RecipeOverviewItem

internal class RecipeOverViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    AsyncListDiffer.ListListener<RecipeOverviewItem> {

    private val asyncDiff = AsyncListDiffer(this, RecipeOverviewDiff())
    private val HEADING = 0
    private val TEXT = 1
    private val RECIPES = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HEADING -> {
                val binding = ComponentTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OverviewTextViewHolder(binding, R.style.TextAppearance_FeastIt_TitleLarge)
            }
            TEXT -> {
                val binding = ComponentTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OverviewTextViewHolder(binding, R.style.TextAppearance_FeastIt_BodyMedium)
            }
            RECIPES -> {
                val binding = ComponentTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OverviewTextViewHolder(binding, R.style.TextAppearance_FeastIt_BodyMedium)
            }
            else -> {
               throw IllegalArgumentException("Invalid view type $viewType")
            }
        }
    }

    override fun getItemCount(): Int = asyncDiff.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when(asyncDiff.currentList[position]) {
            is RecipeOverviewItem.Heading -> HEADING
            is RecipeOverviewItem.Text -> TEXT
            is RecipeOverviewItem.Recipes -> RECIPES
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = asyncDiff.currentList[position]
        when(holder.itemViewType) {
            HEADING -> {
                val adapterItem = section as RecipeOverviewItem.Heading
                (holder as OverviewTextViewHolder).bind(adapterItem.text)
            }
            TEXT -> {
                val adapterItem = section as RecipeOverviewItem.Text
                (holder as OverviewTextViewHolder).bind(adapterItem.value)
            }
            RECIPES -> {
                val adapterItem = section as RecipeOverviewItem.Recipes
                (holder as OverviewTextViewHolder).bind(adapterItem.items.toString())
            }
        }
    }

    fun reload(items: List<RecipeOverviewItem>) {
        with(asyncDiff) {
            removeListListener(this@RecipeOverViewAdapter)
            addListListener(this@RecipeOverViewAdapter)
            submitList(items)
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<RecipeOverviewItem>,
        currentList: MutableList<RecipeOverviewItem>
    ) {}
}
