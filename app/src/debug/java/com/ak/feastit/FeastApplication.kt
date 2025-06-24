package com.ak.feastit

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.work.Configuration
import androidx.work.WorkerFactory
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ak.feastit.core.logging.FileLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
internal class FeastApplication : Application(), Configuration.Provider, ImageLoaderFactory {

//    TODO lazy initialization of workers
    @Inject
    lateinit var workerFactory: WorkerFactory

    private var tree: FileLoggingTree? = null

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupStrictMode()
        Timber.plant(Timber.DebugTree())
//        TODO handle file logging to share logs getting diskviolation strictmode
         FileLoggingTree(this).apply {
             tree = FileLoggingTree(this@FeastApplication)
             Timber.plant(tree!!)
         }
    }

    override fun onTerminate() {
        tree?.onStop()
        super.onTerminate()
    }

    //    TODO lazy initialization of image loader
    override fun newImageLoader(): ImageLoader {
//        TODO clear cache from local
//        this.imageLoader.diskCache?.clear()
//        this.imageLoader.memoryCache?.clear()
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.2)
                    .build()
            }.diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
    }

    private fun setupStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyFlashScreen()
                .penaltyLog()
                .build(),
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
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
                }
                .penaltyLog()
                .build(),
        )
    }
}