package com.ak.feastit.ui.explore.experimental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import coil.load
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentExploreHeaderBinding

internal class ExploreHeaderFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentExploreHeaderBinding.inflate(inflater)

    private val binding: FragmentExploreHeaderBinding
        get() = baseBinding as FragmentExploreHeaderBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        val args = arguments ?: throw IllegalStateException("Nothing to show in pager")
        val image = args.getString(ARGS_IMG_URL)
        val recipeTitle = args.getString(ARGS_TITLE)
        binding.recipeImg.load(image) {
            placeholder(R.drawable.ic_recipe_img_placeholder)
            error(R.drawable.ic_recipe_img_placeholder)
        }
        binding.recipeName.text = recipeTitle
    }
    
    companion object {
        const val ARGS_IMG_URL = "args-recipe-img-url"
        const val ARGS_TITLE = "args-recipe-title"
        fun newInstance(args: Bundle): ExploreHeaderFragment {
            val fragment = ExploreHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}