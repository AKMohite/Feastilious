// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentExploreBinding
import com.ak.feastit.ui.explore.experimental.ExploreItemAction
import com.ak.feastit.ui.explore.experimental.ExploreSectionAdapter
import com.ak.feastit.utils.doOnApplyWindowInsets
import com.ak.feastit.utils.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ExploreFragment : BaseFragment() {
  private val viewModel: ExploreViewModel by viewModels()
  private var adapter: ExploreSectionAdapter? = null

  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentExploreBinding.inflate(inflater)

  private val binding: FragmentExploreBinding
    get() = baseBinding as FragmentExploreBinding

  override fun onViewReady(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }
//        binding.searchView.setReadOnly(focusable = false, inputType = InputType.TYPE_NULL)
    binding.exploreItems.doOnApplyWindowInsets { insetView, insets, _, margins ->
      val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
      insetView.updateLayoutParams<MarginLayoutParams> {
        bottomMargin = margins.bottom + inset
      }
    }
    binding.searchCard.onClick {
      navigateToSearch()
    }
    binding.exploreItems.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        adapter.setEventListener(eventListener)
    adapter =
      ExploreSectionAdapter(
        fragmentManager = this@ExploreFragment.childFragmentManager,
        lifecycle = this.viewLifecycleOwner.lifecycle,
        sectionEvents = ::handleSectionEvents,
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
//          state.errorMessage?.let { message ->
//            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
//          }
        }
      }
    }
  }

  private fun navigateToSearch() {
    Timber.d("navigateToSearch")
    findNavController().navigate(ExploreFragmentDirections.exploreToSearch())
  }

  private fun handleSectionEvents(action: ExploreItemAction) {
    when (action) {
      is ExploreItemAction.ChipClick -> {
        Timber.d("On chip click: ${action.chip}")
        findNavController().navigate(
          ExploreFragmentDirections.exploreToViewAll(
            category = action.chip.type.name,
            categorySubType = action.chip.title,
          ),
        )
      }

      ExploreItemAction.ListUpdate -> {
//                binding.exploreItems.smoothScrollToPosition(0)
      }

      is ExploreItemAction.RecipeClick -> {
        Timber.d("Recipe click: ${action.recipeId}")
                /*exitTransition = MaterialElevationScale(false).apply {
                    duration =
                        resources.getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
                            .toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration =
                        resources.getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
                            .toLong()
                }*/
        val extras =
          FragmentNavigatorExtras(
            *action.sharedElementsVarArgs(),
          )
        findNavController().navigate(
          directions =
          ExploreFragmentDirections.exploreToRecipeDetail(
            recipeId = action.recipeId,
          ),
          navigatorExtras = extras,
        )
      }

      is ExploreItemAction.ViewAll -> {
        Timber.d("View all: ${action.category}")
        findNavController().navigate(ExploreFragmentDirections.exploreToViewAll(category = action.category.name))
      }
    }
  }

  override fun onDestroyView() {
//        adapter.setEventListener(null)
    adapter = null
    super.onDestroyView()
  }
}
