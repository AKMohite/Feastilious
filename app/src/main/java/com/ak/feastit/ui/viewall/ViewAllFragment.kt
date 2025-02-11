package com.ak.feastit.ui.viewall

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
import com.ak.feastit.databinding.FragmentViewAllBinding
import com.ak.feastit.utils.getEnumTitle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ViewAllFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentViewAllBinding.inflate(inflater)

    private val binding: FragmentViewAllBinding
        get() = baseBinding as FragmentViewAllBinding

    private var adapter: ViewAllAdapter? = null

    private val viewModel: ViewAllViewmodel by viewModels()

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        val category = viewModel.getPageTitle()
        binding.categoryTypeTxt.text = category
        adapter = ViewAllAdapter(onRecipeClick = { recipeId ->

        })
        binding.viewAllItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.viewAllItems.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
//                        adapter?.submitData(state.recipes)
                    }
                }
                launch {
                    viewModel.observePagedRecipes().collectLatest { pagingData ->
                        adapter?.submitData(pagingData)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}
