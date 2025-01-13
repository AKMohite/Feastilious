package com.ak.feastit.ui.yumdetail.tabs.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabNutritionBinding
import com.ak.feastit.ui.yumdetail.YumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class RecipeNutritionFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabNutritionBinding.inflate(inflater)

    private val binding: FragmentDetailTabNutritionBinding
        get() = baseBinding as FragmentDetailTabNutritionBinding

    private val viewModel: YumDetailViewModel by viewModels({ requireParentFragment() })

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
