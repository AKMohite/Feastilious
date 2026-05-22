// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.di

import android.app.Application
import com.ak.feastit.compose.worker.BackgroundScheduler
import com.ak.feastit.compose.worker.WorkerScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal object WorkerModule {
  @Provides
  fun provideWorkerScheduler(app: Application): WorkerScheduler = BackgroundScheduler(app)
}
