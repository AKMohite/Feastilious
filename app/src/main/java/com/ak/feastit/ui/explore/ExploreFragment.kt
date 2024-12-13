package com.ak.feastit.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExploreFragment : BaseFragment() {

    private val viewModel: ExploreViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentExploreBinding.inflate(inflater)

    private val binding: FragmentExploreBinding
        get() = baseBinding as FragmentExploreBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val sections = state.displayableSections()
                }
            }
        }
    }
}