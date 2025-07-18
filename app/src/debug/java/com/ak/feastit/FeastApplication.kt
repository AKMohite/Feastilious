// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.work.Configuration
import androidx.work.WorkerFactory
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.util.DebugLogger
import com.ak.feastit.core.logging.FileLoggingTree
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
internal class FeastApplication :
  Application(),
  Configuration.Provider,
  SingletonImageLoader.Factory {
  //    TODO lazy initialization of workers
  @Inject
  lateinit var workerFactory: WorkerFactory

  @Inject
  lateinit var imageInterceptor: Interceptor

  private var tree: FileLoggingTree? = null

  override val workManagerConfiguration: Configuration
    get() =
      Configuration
        .Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.VERBOSE)
        .build()

  override fun onCreate() {
    super.onCreate()
    setupStrictMode()
    Timber.plant(Timber.DebugTree())
//        TODO handle file logging to share logs getting diskviolation strictmode
    FileLoggingTree(this).apply {
      tree = this
      Timber.plant(this)
    }
  }

  override fun onTerminate() {
    tree?.onStop()
    super.onTerminate()
  }

  private fun setupStrictMode() {
    StrictMode.setThreadPolicy(
      ThreadPolicy
        .Builder()
        .detectAll()
        .penaltyFlashScreen()
        .penaltyLog()
        .build(),
    )
    StrictMode.setVmPolicy(
      VmPolicy
        .Builder()
        .detectLeakedSqlLiteObjects()
        .detectActivityLeaks()
        .detectLeakedClosableObjects()
        .detectLeakedRegistrationObjects()
        .detectFileUriExposure()
        .detectCleartextNetwork()
        .apply {
          if (Build.VERSION.SDK_INT >= 26) {
            detectContentUriWithoutPermission()
          }
          if (Build.VERSION.SDK_INT >= 29) {
            detectCredentialProtectedWhileLocked()
          }
          if (Build.VERSION.SDK_INT >= 31) {
            detectIncorrectContextUse()
            detectUnsafeIntentLaunch()
          }
        }.penaltyLog()
        .build(),
    )
  }

  // TODO lazy initialization of image loader
  override fun newImageLoader(context: PlatformContext): ImageLoader {
    //        TODO clear cache from local
//        this.imageLoader.diskCache?.clear()
//        this.imageLoader.memoryCache?.clear()
    return ImageLoader
      .Builder(this)
      .components {
        add(imageInterceptor)
      }
      .memoryCache {
        MemoryCache
          .Builder()
          .maxSizePercent(this, 0.2)
          .build()
      }.diskCache {
        DiskCache
          .Builder()
          .directory(cacheDir.resolve("image_cache"))
          .maxSizeBytes(5 * 1024 * 1024)
          .build()
      }.logger(DebugLogger())
//      .respectCacheHeaders(false)
      .build()
  }
}
