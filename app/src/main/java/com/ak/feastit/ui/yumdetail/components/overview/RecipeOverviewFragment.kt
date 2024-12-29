package com.ak.feastit.ui.yumdetail.components.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabOverviewBinding

internal class RecipeOverviewFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabOverviewBinding.inflate(inflater)

    private val binding: FragmentDetailTabOverviewBinding
        get() = baseBinding as FragmentDetailTabOverviewBinding


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
