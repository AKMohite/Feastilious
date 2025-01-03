package com.ak.feastit.di

import android.app.Application
import com.ak.feastit.worker.BackgroundScheduler
import com.ak.feastit.worker.WorkerScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object WorkerModule {

    @Provides
    fun provideWorkerScheduler(
        app: Application
    ): WorkerScheduler = BackgroundScheduler(app)

//    @Binds
//    abstract fun bindWorkerFactory(workerFactory: YumWorkerFactory): WorkerFactory

}