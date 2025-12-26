// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.util.DebugLogger

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

  override val workManagerConfiguration: Configuration
    get() =
      Configuration
        .Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()

  override fun onCreate() {
    super.onCreate()
    Timber.plant(CrashlyticsTree)
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
      }
//      .respectCacheHeaders(false)
      .build()
  }
}

object CrashlyticsTree : Timber.Tree() {
  override fun log(
    priority: Int,
    tag: String?,
    message: String,
    t: Throwable?,
  ) {
  }
}
