package com.ak.feastit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
internal class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? = FragmentSearchBinding.inflate(inflater)

    private val binding: FragmentSearchBinding
        get() = baseBinding as FragmentSearchBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        binding.openSearchBar.inflateMenu(R.menu.recipe_search)
        binding.searchView.inflateMenu(R.menu.recipe_search)
        // this is to launch search view
//        binding.openSearchBar.performClick()
        binding.openSearchBar.setOnMenuItemClickListener { menuItem ->
            onMenuItemClick(menuItem)
            return@setOnMenuItemClickListener true
        }
        binding.searchView.setOnMenuItemClickListener { menuItem ->
            onMenuItemClick(menuItem)
            return@setOnMenuItemClickListener true
        }
        viewModel.reload()
//        show recent searches and trending recipes in search expanded view and if there is query filter recent searches and search in local db
//        show results in collapsed view with grid same as view all
    }

    private fun onMenuItemClick(menuItem: MenuItem?) {
        when(menuItem?.itemId) {
            R.id.search_filter -> {
//                open filters bottom sheet
            }
            R.id.search_image -> {
//                check permissions and open bottom sheet for camera and gallery pick
            }
        }
    }

    override fun onDestroyView() {
        binding.openSearchBar.menu.clear()
        binding.searchView.toolbar.menu.clear()
        super.onDestroyView()
    }

}