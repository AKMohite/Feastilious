package com.ak.feastit.ui.yumdetail.tabs.nutrition

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

internal class LegendRowView(context: Context) : LinearLayout(context) {
  private val dot: TextView
  private val labelView: TextView
  private val percentView: TextView

  init {
    orientation = HORIZONTAL
    gravity = Gravity.CENTER_VERTICAL
    val padding = dp(4)
    setPadding(padding, padding, padding, padding)

    dot = TextView(context).apply {
      val size = dp(12)
      layoutParams = LayoutParams(size, size).apply { marginEnd = dp(8) }
      background = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(0)
      }
    }

    labelView = TextView(context).apply {
      layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
      setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
    }

    percentView = TextView(context).apply {
      layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
      setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
    }

    addView(dot)
    addView(labelView)
    addView(percentView)
  }

  fun bind(colorInt: Int, label: String, percent: Double) {
    (dot.background as GradientDrawable).setColor(colorInt)
    labelView.text = label
    percentView.text = String.format("%.1f%%", percent)
  }

  private fun dp(v: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    v.toFloat(),
    resources.displayMetrics,
  ).toInt()
}


