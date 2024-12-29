package com.ak.feastit.ui.yumdetail.components.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabInstructionsBinding

internal class RecipeInstructionsFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabInstructionsBinding.inflate(inflater)

    private val binding: FragmentDetailTabInstructionsBinding
        get() = baseBinding as FragmentDetailTabInstructionsBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
