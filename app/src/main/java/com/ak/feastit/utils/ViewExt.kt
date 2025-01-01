package com.ak.feastit.utils

import android.os.SystemClock
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

internal fun View.show(toShow: Boolean, gone: Boolean = true) {
    if (toShow) {
        this.show()
    } else {
        this.hide(gone)
    }
}

internal fun View.show() {
    visibility = View.VISIBLE
}

internal fun View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

internal fun View.onClick(callback: () -> Unit) {
    this.setOnClickListener(
        DebouncingOnClickListener(
            threshold = DebouncingOnClickListener.DEFAULT_THRESHOLD,
            doClick = callback
        )
    )
}

private class DebouncingOnClickListener(
    private val threshold: Long,
    private val doClick: () -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val clickTime = SystemClock.elapsedRealtime()
        // Prevent mis-clicking
        if (clickTime - lastClickTime < threshold) {
            return
        }
        lastClickTime = clickTime
        doClick.invoke()
    }

    companion object {
        const val DEFAULT_THRESHOLD: Long = 500
    }
}

internal fun SearchView.setReadOnly(focusable: Boolean = true, inputType: Int = InputType.TYPE_CLASS_TEXT) {
    isFocusable = !focusable
    isFocusableInTouchMode = !focusable
    this.inputType = inputType
}

/**
 * https://chris.banes.dev/2019/04/12/insets-listeners-to-layouts/
 */
internal fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

/**
 * https://chris.banes.dev/2019/04/12/insets-listeners-to-layouts/
 */
internal fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialSpacing, InitialSpacing) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding, initialMargin)
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    InitialSpacing(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom,
    )

private fun recordInitialMarginForView(view: View): InitialSpacing {
    val lp = view.layoutParams as? ViewGroup.MarginLayoutParams
    return InitialSpacing(
        lp?.leftMargin ?: 0,
        lp?.topMargin ?: 0,
        lp?.rightMargin ?: 0,
        lp?.bottomMargin ?: 0,
    )
}

internal data class InitialSpacing(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
)