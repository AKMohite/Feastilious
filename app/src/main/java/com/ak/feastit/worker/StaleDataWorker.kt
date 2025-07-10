package com.ak.feastit.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mak.feastit.domain.repository.StaleRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
internal class StaleDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: StaleRepository,
    private val dispatcher: DispatcherProvider
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher.io) {
        repository.removeStaleData()
        return@withContext Result.success()
    }

}
