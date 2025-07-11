// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
internal class FeastApplication :
  Application(),
  Configuration.Provider,
  ImageLoaderFactory {
  //    TODO lazy initialization of workers
  @Inject
  lateinit var workerFactory: WorkerFactory

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

  //    TODO lazy initialization of image loader
  override fun newImageLoader(): ImageLoader {
//        TODO clear cache from local
//        this.imageLoader.diskCache?.clear()
//        this.imageLoader.memoryCache?.clear()
    return ImageLoader
      .Builder(this)
      .memoryCache {
        MemoryCache
          .Builder(this)
          .maxSizePercent(0.2)
          .build()
      }.diskCache {
        DiskCache
          .Builder()
          .directory(cacheDir.resolve("image_cache"))
          .maxSizeBytes(5 * 1024 * 1024)
          .build()
      }.respectCacheHeaders(false)
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
