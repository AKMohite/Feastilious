package com.ak.feastit.ui.explore

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
import com.ak.feastit.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExploreFragment : BaseFragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private val adapter: ExploreSectionAdapter by lazy { ExploreSectionAdapter() }

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentExploreBinding.inflate(inflater)

    private val binding: FragmentExploreBinding
        get() = baseBinding as FragmentExploreBinding

    private val eventListener by lazy {
        object : SectionEventListener {
            override fun viewAll(category: ExploreCategory) {
                if (ExploreCategory.getRefreshExploreEntries().contains(category)) {
//                navigate to pagination/search screen
                } else {
                    throw IllegalStateException("$category cannot have more items to load")
                }
            }

            override fun onRecipeClick(recipeId: Long) {
//            navigate to recipe details screen
            }

            override fun onChipClick(chip: ExploreChip) {
//            navigate to pagination/search screen
            }
        }
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        binding.exploreItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.setEventListener(eventListener)
        binding.exploreItems.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val sections = state.displayableSections()
                    adapter.submitList(sections)
                }
            }
        }
    }

    override fun onDestroyView() {
        adapter.setEventListener(null)
        super.onDestroyView()
    }
}