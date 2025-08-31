package com.ak.feastit.ui.yumdetail.tabs.nutrition

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ak.feastit.R

internal class CaloriePieChartView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

  // Pair<ColorInt, fraction>
  private var segments: List<Pair<Int, Float>> = emptyList()

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeWidth = 48f }
  private val rect = RectF()

  fun setData(breakdown: Map<String, Double>) {
    if (breakdown.isEmpty()) {
      segments = emptyList()
      invalidate()
      return
    }
    val entries = breakdown.toList()
    val total = entries.sumOf { it.second }.takeIf { it > 0.0 } ?: 1.0
    segments = entries.map { (label, value) ->
      colorFor("$label-$value") to (value / total).toFloat()
    }
    invalidate()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val size = minOf(width, height)
    val pad = 32f
    rect.set(pad, pad, size - pad, size - pad)

    var start = -90f
    for ((colorInt, fraction) in segments) {
      paint.color = colorInt
      val sweep = fraction * 360f
      canvas.drawArc(rect, start, sweep, false, paint)
      start += sweep
    }
  }

  companion object {
    fun colorFor(label: String): Int {
      val hash = (label.hashCode().toLong() and 0xFFFFFFFF).toInt()
      val hue = (hash % 360).toFloat()
      val saturation = 0.65f
      val value = 0.95f
      return Color.HSVToColor(floatArrayOf(hue, saturation, value))
    }
  }
}


