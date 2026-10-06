// Copyright 2026, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.benchmark

import android.content.ComponentName
import android.content.Intent
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrivialBaselineBenchmark {

  @get:Rule
  val rule = BaselineProfileRule()

  @Test
  fun trivialStartup() {
    trackStartup()
  }

  private fun trackStartup() {
    rule.collect(
      packageName = APP_TO_BENCHMARK,
    ) {
      val intent = Intent()
      intent.component = ComponentName(
        /* pkg = */
        APP_TO_BENCHMARK,
        /* cls = */
        "com.ak.feastit.ui.main.MainActivity",
      )
      startActivityAndWait(intent)
    }
  }

  companion object {
    const val APP_TO_BENCHMARK = "com.ak.feastit"
  }
}
