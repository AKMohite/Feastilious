package com.ak.feastit.ui.yumdetail.components.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabIngredientsBinding
import com.ak.feastit.ui.yumdetail.YumDetailViewModel
import com.ak.feastit.ui.yumdetail.components.ingredients.component.RecipeIngredientsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class RecipeIngredientsFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabIngredientsBinding.inflate(inflater)

    private val binding: FragmentDetailTabIngredientsBinding
        get() = baseBinding as FragmentDetailTabIngredientsBinding

    private val viewModel: YumDetailViewModel by viewModels({ requireParentFragment() })

    private val adapter: RecipeIngredientsAdapter by lazy {
        RecipeIngredientsAdapter()
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        binding.recipeIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeIngredients.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        adapter.reload(state.ingredients)
                    }
                }
            }
        }
    }

}
