package com.ak.feastit.ui.recipedetail

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.RecipeHeadingViewBinding

class RecipeHeadingAdapter(private val heading: String) : RecyclerView.Adapter<RecipeHeadingAdapter.HeadingViewHolder>() {

    var subtitle: Spanned? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HeadingViewHolder(private val binding: RecipeHeadingViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(heading: String, subTitle: Spanned?) {
            binding.tvHeadingTitle.text = heading
            if (!subTitle.isNullOrBlank()) {
                binding.tvSubTitle.text = subTitle
                binding.tvSubTitle.visibility = View.VISIBLE
            } else {
                binding.tvSubTitle.visibility =  View.GONE
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadingViewHolder {
        val binding = RecipeHeadingViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadingViewHolder(binding)
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: HeadingViewHolder, position: Int) {
        holder.bindData(heading, subtitle)
    }

}