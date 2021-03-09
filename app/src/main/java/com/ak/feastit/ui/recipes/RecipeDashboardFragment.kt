package com.ak.feastit.ui.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ak.feastit.R
import com.ak.feastit.utils.APP_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipeDashboardFragment : Fragment(R.layout.fragment_recipe_dashboard) {

    private val viewModel: RecipeViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setFlowObservers()
    }

    private fun setFlowObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.allCategories.collect { categories ->
                categories.forEach {category ->
                    Log.d(APP_TAG, "onActivityCreated: ${category.categoryName}")
                }
            }
        }
    }
}