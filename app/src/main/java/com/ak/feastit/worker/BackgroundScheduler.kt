package com.ak.feastit.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val STALE_DATA_WORK = "stale-data-worker"
private const val MEAL_NOTIFY_WORK = "meal-notify-worker"

// TODO check workers time span and status
internal class BackgroundScheduler @Inject constructor(
    @ApplicationContext private val context: Context
): WorkerScheduler {

    // region stale data
    override fun scheduleStaleWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()
        val work = PeriodicWorkRequestBuilder<StaleDataWorker>(21, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(STALE_DATA_WORK)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(STALE_DATA_WORK, ExistingPeriodicWorkPolicy.KEEP, work)


//        val work1 = OneTimeWorkRequestBuilder<StaleDataWorker>()
//            .setConstraints(Constraints.Builder().build())
//            .addTag("test_one")
//            .build()
//        WorkManager.getInstance(context)
//            .enqueueUniqueWork("test_one", ExistingWorkPolicy.KEEP, work1)
    }

    override fun rescheduleStale() {
        cancelStaleWorker()
        scheduleStaleWorker()
    }

    override fun cancelStaleWorker() {
        WorkManager.getInstance(context).cancelUniqueWork(STALE_DATA_WORK)
    }
    // endregion

    // region meal notify
    override fun scheduleMealWorker() {
//        val mealId = 0L
//        val delaySeconds = 0L
//        val constraints = Constraints.Builder()
//            .setRequiresBatteryNotLow(true)
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .build()
//        val work = OneTimeWorkRequestBuilder<MealNotifyWorker>()
//            .setConstraints(constraints)
//            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
//            .addTag(MEAL_NOTIFY_WORK)
//            .build()
//        WorkManager.getInstance(context)
//            .enqueueUniqueWork("${MEAL_NOTIFY_WORK}-$mealId", ExistingWorkPolicy.KEEP, work)
    }

    override fun rescheduleMealWorker() {
        cancelMealWorker()
        scheduleMealWorker()
    }

    override fun cancelMealWorker() {
        WorkManager.getInstance(context).cancelAllWorkByTag(MEAL_NOTIFY_WORK)
    }
    // endregion
}

interface WorkerScheduler {
    fun scheduleStaleWorker()
    fun rescheduleStale()
    fun cancelStaleWorker()

    fun scheduleMealWorker()
    fun rescheduleMealWorker()
    fun cancelMealWorker()
}
