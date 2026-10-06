// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.utils

import kotlin.math.pow
import kotlin.math.round

fun Double.padding(decimalPrecision: Int): Double {
  val scale = 10.0.pow(decimalPrecision)
  return round(this * scale) / scale
}
