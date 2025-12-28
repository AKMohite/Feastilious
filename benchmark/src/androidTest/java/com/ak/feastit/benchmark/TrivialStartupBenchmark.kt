package com.ak.feastit.benchmark

import android.content.ComponentName
import android.content.Intent
import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrivialStartupBenchmark {

  @get:Rule
  val rule = MacrobenchmarkRule()

  @Test
  fun trivialStartupNoProfile() {
    trackStartup(CompilationMode.None())
  }

  @Test
  fun trivialStartupWithProfile() {
    trackStartup(CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require))
  }

  private fun trackStartup(mode: CompilationMode) {
    rule.measureRepeated(
      packageName = APP_TO_BENCHMARK,
      metrics = listOf(StartupTimingMetric()),
      startupMode = StartupMode.COLD,
      compilationMode = mode, // don't care yet
      iterations = 1,
      setupBlock = {
//        killProcess()
        pressHome()
      },
      measureBlock = {
        val intent = Intent()
        intent.component = ComponentName(
          /* pkg = */ APP_TO_BENCHMARK,
          /* cls = */ "com.ak.feastit.ui.main.MainActivity"
        )
        startActivityAndWait(intent)
      }
    )
  }

  companion object {
    const val APP_TO_BENCHMARK = "com.ak.feastit"
  }

}
