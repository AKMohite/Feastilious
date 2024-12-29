package com.ak.feastit.ui.yumdetail.components.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentDetailTabNutritionBinding

internal class RecipeNutritionFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentDetailTabNutritionBinding.inflate(inflater)

    private val binding: FragmentDetailTabNutritionBinding
        get() = baseBinding as FragmentDetailTabNutritionBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {

    }

}
