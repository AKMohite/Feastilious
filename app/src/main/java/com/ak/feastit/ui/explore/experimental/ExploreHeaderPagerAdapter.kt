// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore.experimental

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mak.feastit.domain.model.Recipe

internal class ExploreHeaderPagerAdapter(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
  private val items: List<Recipe>,
  private val sectionEvents: ((ExploreItemAction) -> Unit)? = null,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
  override fun getItemCount(): Int = items.size

  override fun createFragment(position: Int): Fragment {
    val recipe = items[position]
    val args = Bundle()
    args.putString(ExploreHeaderFragment.ARGS_IMG_URL, recipe.image)
    args.putString(ExploreHeaderFragment.ARGS_TITLE, recipe.name)
    args.putLong(ExploreHeaderFragment.ARGS_RECIPE_ID, recipe.id)
    return ExploreHeaderFragment.newInstance(args, sectionEvents)
  }

//    override fun onBindViewHolder(
//        holder: FragmentViewHolder,
//        position: Int,
//        payloads: MutableList<Any>
//    ) {
//        holder.itemView.onClick { sectionEvents(ExploreItemAction.RecipeClick(items[position].id)) }
//    }
}
