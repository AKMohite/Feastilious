package com.ak.feastit.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentExploreBinding
import com.ak.feastit.ui.explore.experimental.ExploreItemAction
import com.ak.feastit.ui.explore.experimental.ExploreSectionAdapter
import com.ak.feastit.utils.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class ExploreFragment : BaseFragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private var adapter: ExploreSectionAdapter? = null

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
//        binding.searchView.setReadOnly(focusable = false, inputType = InputType.TYPE_NULL)
        binding.searchCard.onClick {
            navigateToSearch()
        }
        binding.exploreItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        adapter.setEventListener(eventListener)
        adapter = ExploreSectionAdapter(
            fragmentManager = this@ExploreFragment.childFragmentManager,
            lifecycle = this.viewLifecycleOwner.lifecycle,
            sectionEvents = ::handleSectionEvents
//            sectionEvents = null
        ).apply {
//            TODO add to all adapters?
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.exploreItems.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val sections = state.displayableSections()
                    adapter?.submitList(sections)
                }
            }
        }
    }

    private fun navigateToSearch() {
        Timber.d("navigateToSearch")
    }

    private fun handleSectionEvents(action: ExploreItemAction) {
        when(action) {
            is ExploreItemAction.ChipClick -> {}
            ExploreItemAction.ListUpdate -> {
//                binding.exploreItems.smoothScrollToPosition(0)
            }
            is ExploreItemAction.RecipeClick -> {
                findNavController().navigate(
                    ExploreFragmentDirections.exploreToRecipeDetail(
                    recipeId = action.recipeId
                ))
            }
            is ExploreItemAction.ViewAll -> {}
        }
    }

    override fun onDestroyView() {
//        adapter.setEventListener(null)
        adapter = null
        super.onDestroyView()
    }
}