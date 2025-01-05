package com.ak.feastit.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class FavoritesFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentFavoritesBinding.inflate(inflater)

    private val binding: FragmentFavoritesBinding
        get() = baseBinding as FragmentFavoritesBinding

    private val viewmodel: FavoriteViewModel by viewModels()

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        observers()
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collectLatest { state ->

                }
            }
        }
    }

}