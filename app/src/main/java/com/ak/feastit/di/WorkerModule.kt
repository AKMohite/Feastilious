// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.di

import android.app.Application
import androidx.work.WorkerFactory
import com.ak.feastit.worker.BackgroundScheduler
import com.ak.feastit.worker.WorkerScheduler
import com.ak.feastit.worker.YumWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object WorkerModule {
  @Provides
  fun provideWorkerScheduler(app: Application): WorkerScheduler = BackgroundScheduler(app)

//    @Binds
//    abstract fun bindWorkerFactory(workerFactory: YumWorkerFactory): WorkerFactory
}

@Module
@InstallIn(SingletonComponent::class)
internal interface AppWorkerModule {
  @Binds
  abstract fun bindWorkerFactory(workerFactory: YumWorkerFactory): WorkerFactory
}
