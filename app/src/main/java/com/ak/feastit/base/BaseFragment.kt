package com.ak.feastit.base

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R

abstract class BaseFragment: Fragment() {

    protected open fun getViewBinding(inflater: LayoutInflater): ViewBinding? = null
    var baseBinding: ViewBinding? = null
        private set

    @StyleRes
    protected open val themeId: Int = R.style.Theme_FeastIt

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflateFromViewBinding(getInflater(inflater), container, savedInstanceState)
            ?: throw IllegalStateException(
                "Fragment must either provide a layoutId OR override inflateFromViewBinding method. None are provided for ${this::class.simpleName}"
            )
        return view
    }

    protected open fun inflateFromViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = getViewBinding(inflater)
        return baseBinding?.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(view, savedInstanceState)
    }

    abstract fun onViewReady(view: View, savedInstanceState: Bundle?)

    override fun onDestroyView() {
        baseBinding = null
        super.onDestroyView()
    }

    private fun getInflater(inflater: LayoutInflater) =
        inflater.cloneInContext(ContextThemeWrapper(activity, themeId))
}