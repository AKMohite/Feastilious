package com.ak.feastit.ui.yumdetail.tabs.instructions

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
import com.ak.feastit.databinding.FragmentDetailTabInstructionsBinding
import com.ak.feastit.ui.yumdetail.YumDetailViewModel
import com.ak.feastit.ui.yumdetail.tabs.instructions.component.RecipeInstructionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class RecipeInstructionsFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabInstructionsBinding.inflate(inflater)

    private val binding: FragmentDetailTabInstructionsBinding
        get() = baseBinding as FragmentDetailTabInstructionsBinding

    private val viewModel: YumDetailViewModel by viewModels({ requireParentFragment() })

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        binding.recipeInstructions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = RecipeInstructionsAdapter {
            viewModel.toggleMealPlan()
        }
        binding.recipeInstructions.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        adapter.reload(state.instructions)
                    }
                }
            }
        }
    }

}
