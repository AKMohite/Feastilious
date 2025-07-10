package com.ak.feastit.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ak.feastit.core.notification.NotificationManager
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.repository.StaleRepository
import com.mak.feastit.domain.util.DispatcherProvider
import javax.inject.Inject

internal class YumWorkerFactory @Inject constructor(
    private val staleRepository: StaleRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val notificationManager: NotificationManager,
    private val dispatcher: DispatcherProvider
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerKlass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
//        val constructor = workerKlass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
//        return when(val instance = constructor.newInstance(appContext, workerParameters)) {
//            is StaleDataWorker -> StaleDataWorker(
//                appContext = appContext,
//                workerParams = workerParameters,
//                repository = repository,
//                dispatcher = dispatcher
//            )
//            else -> throw IllegalArgumentException("Does not have instance for worker: $instance")
//        }
        return when(workerKlass) {
            StaleDataWorker::class.java -> StaleDataWorker(
                appContext = appContext,
                workerParams = workerParameters,
                repository = staleRepository,
                dispatcher = dispatcher
            )
            MealPlanNotifyWorker::class.java -> MealPlanNotifyWorker(
                appContext = appContext,
                workerParams = workerParameters,
                mealPlanRepository = mealPlanRepository,
                notificationManager = notificationManager
            )
//            else -> throw IllegalArgumentException("Does not have instance for worker: $workerKlass")
            else -> null
        }
    }

}