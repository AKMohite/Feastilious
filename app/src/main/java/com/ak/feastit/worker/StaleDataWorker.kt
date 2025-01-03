package com.ak.feastit.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mak.feastit.domain.repository.StaleRepository
import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.withContext

@HiltWorker
internal class StaleDataWorker(
    private val repository: StaleRepository,
    private val dispatcher: DispatcherProvider,
    appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher.io) {
        repository.removeStaleData()
        return@withContext Result.success()
    }

}