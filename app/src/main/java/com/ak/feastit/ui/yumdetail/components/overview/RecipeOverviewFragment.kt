package com.ak.feastit.ui.yumdetail.components.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabOverviewBinding
import com.ak.feastit.ui.yumdetail.YumDetailViewModel

internal class RecipeOverviewFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabOverviewBinding.inflate(inflater)

    private val binding: FragmentDetailTabOverviewBinding
        get() = baseBinding as FragmentDetailTabOverviewBinding

    private val viewModel: YumDetailViewModel by viewModels({ requireParentFragment() })


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
