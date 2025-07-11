// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore.experimental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import coil.load
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentExploreHeaderBinding
import com.ak.feastit.utils.onClick

internal class ExploreHeaderFragment : BaseFragment() {
  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentExploreHeaderBinding.inflate(inflater)

  private var sectionEvents: ((ExploreItemAction) -> Unit)? = null
  private val binding: FragmentExploreHeaderBinding
    get() = baseBinding as FragmentExploreHeaderBinding

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    val args = arguments ?: throw IllegalStateException("Nothing to show in pager")
    val image = args.getString(ARGS_IMG_URL)
    val recipeTitle = args.getString(ARGS_TITLE)
    val recipeId = args.getLong(ARGS_RECIPE_ID)
    val imgTransition = getString(R.string.recipe_to_detail_image, recipeId)
    val nameTransition = getString(R.string.recipe_to_detail_name, recipeId)
    binding.recipeImg.transitionName = imgTransition
    binding.recipeName.transitionName = nameTransition
    val sharedElements: Map<View, String> =
      mapOf(
        binding.recipeImg to imgTransition,
        binding.recipeName to nameTransition,
      )
    binding.recipeImg.load(image) {
      placeholder(R.drawable.ic_recipe_img_placeholder)
      error(R.drawable.ic_recipe_img_placeholder)
    }
    binding.recipeName.text = recipeTitle
    binding.root.onClick {
      sectionEvents?.invoke(
        ExploreItemAction.RecipeClick(
          sharedElements,
          recipeId,
        ),
      )
    }
  }

  companion object {
    const val ARGS_IMG_URL = "args-recipe-img-url"
    const val ARGS_TITLE = "args-recipe-title"
    const val ARGS_RECIPE_ID = "args-recipe-id"

    fun newInstance(
      args: Bundle,
      sectionEvents: ((ExploreItemAction) -> Unit)? = null,
    ): ExploreHeaderFragment {
      val fragment = ExploreHeaderFragment()
      fragment.arguments = args
      fragment.sectionEvents = sectionEvents
      return fragment
    }
  }
}
