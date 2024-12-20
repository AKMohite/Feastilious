package com.ak.feastit.ui.explore.experimental

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mak.feastit.domain.model.Recipe

class ExploreHeaderPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val items: List<Recipe>
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        val recipe = items[position]
        val args = Bundle()
        args.putString(ExploreHeaderFragment.ARGS_IMG_URL, recipe.recipeImgUrl)
        args.putString(ExploreHeaderFragment.ARGS_TITLE, recipe.recipeName)
        return ExploreHeaderFragment.newInstance(args)
    }
}