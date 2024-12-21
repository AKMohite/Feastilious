package com.ak.feastit.utils

import android.os.SystemClock
import android.text.InputType
import android.view.View
import androidx.appcompat.widget.SearchView

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