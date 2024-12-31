package com.ak.feastit.ui.yumdetail.components.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabOverviewBinding
import com.ak.feastit.ui.yumdetail.YumDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class RecipeOverviewFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabOverviewBinding.inflate(inflater)

    private val binding: FragmentDetailTabOverviewBinding
        get() = baseBinding as FragmentDetailTabOverviewBinding

    private val viewModel: YumDetailViewModel by viewModels({ requireParentFragment() })


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val overview = state.overview ?: return@collectLatest
                    binding.recipeSummary.text = HtmlCompat.fromHtml(overview.recipeSummary, HtmlCompat.FROM_HTML_MODE_COMPACT)
                }
            }
        }
    }

}
