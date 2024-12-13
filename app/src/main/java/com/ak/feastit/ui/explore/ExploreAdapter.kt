package com.ak.feastit.ui.explore

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.ui.recipes.ExploreSection

internal class ExploreAdapter: RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    private val sections: List<ExploreSection> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

}
