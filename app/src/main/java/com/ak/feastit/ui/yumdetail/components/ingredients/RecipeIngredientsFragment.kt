package com.ak.feastit.ui.yumdetail.components.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabIngredientsBinding

internal class RecipeIngredientsFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabIngredientsBinding.inflate(inflater)

    private val binding: FragmentDetailTabIngredientsBinding
        get() = baseBinding as FragmentDetailTabIngredientsBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
