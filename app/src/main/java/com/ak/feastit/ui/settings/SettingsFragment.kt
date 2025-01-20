package com.ak.feastit.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentSettingsBinding

internal class SettingsFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? {
        return FragmentSettingsBinding.inflate(inflater)
    }

    private val binding: FragmentSettingsBinding
        get() = baseBinding as FragmentSettingsBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
    }
}