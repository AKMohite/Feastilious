package com.ak.feastit.ui.yumdetail.components.overview.component

import androidx.annotation.StyleRes
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentTextItemBinding

internal class OverviewTextViewHolder(
    private val binding: ComponentTextItemBinding,
    @StyleRes private val textAppearance: Int
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.itemText.setTextAppearance(textAppearance)
        binding.itemText.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
}
