package com.ak.feastit.ui.viewall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentViewAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ViewAllFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentViewAllBinding.inflate(inflater)

    private val binding: FragmentViewAllBinding
        get() = baseBinding as FragmentViewAllBinding

    private val viewModel: ViewAllViewmodel by viewModels()

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
    }
}
