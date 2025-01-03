package com.ak.feastit.ui.main

import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.worker.WorkerScheduler
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val workScheduler: WorkerScheduler
): BaseViewModel(dispatcher) {

    fun reload() {
        workScheduler.scheduleStaleWorker()
    }
}

