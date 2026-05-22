// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.toInstant
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlinx.datetime.LocalDateTime
import timber.log.Timber

private const val STALE_DATA_WORK = "stale-data-worker"
private const val MEAL_NOTIFY_WORK = "meal-notify-worker"

internal class BackgroundScheduler
@Inject
constructor(
  @ApplicationContext private val context: Context,
) : WorkerScheduler {
  override fun scheduleStaleWorker() {
    val constraints =
      Constraints
        .Builder()
        .setRequiresBatteryNotLow(true)
        .build()
    val work =
      PeriodicWorkRequestBuilder<StaleDataWorker>(20, TimeUnit.DAYS)
        .setConstraints(constraints)
        .addTag(STALE_DATA_WORK)
        .build()
    WorkManager
      .getInstance(context)
      .enqueueUniquePeriodicWork(STALE_DATA_WORK, ExistingPeriodicWorkPolicy.KEEP, work)
  }

  override fun rescheduleStale() {
    cancelStaleWorker()
    scheduleStaleWorker()
  }

  override fun cancelStaleWorker() {
    WorkManager.getInstance(context).cancelUniqueWork(STALE_DATA_WORK)
  }

  override fun scheduleMealWorker(
    mealId: Long,
    notificationDateTime: LocalDateTime,
  ) {
    val constraints =
      Constraints
        .Builder()
        .build()
    val now = defaultNow()
    val workerTime = notificationDateTime.toInstant().minus(1.hours)
    val diff = workerTime.minus(now)
    if (diff.isNegative() && diff.inWholeHours > 1) {
      Timber.d("The meal notification time is in the past")
      return
    }
    val delaySeconds = if (diff.isNegative()) 5 else diff.inWholeSeconds
    Timber.d("Notification delayed for $delaySeconds seconds")
    val data =
      Data
        .Builder()
        .apply {
          putLong(MealPlanNotifyWorker.WORK_MEAL_ID, mealId)
          putString(MealPlanNotifyWorker.WORK_NOTIFICATION_TIME, notificationDateTime.toString())
        }.build()
    val work =
      OneTimeWorkRequestBuilder<MealPlanNotifyWorker>()
        .setConstraints(constraints)
        .setInputData(data)
        .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
        .addTag(MEAL_NOTIFY_WORK)
        .build()
    WorkManager
      .getInstance(context)
      .enqueueUniqueWork("${MEAL_NOTIFY_WORK}-$mealId", ExistingWorkPolicy.REPLACE, work)
  }

  override fun cancelMealWorker(mealId: Long) {
    WorkManager
      .getInstance(context)
      .cancelUniqueWork("${MEAL_NOTIFY_WORK}-$mealId")
  }
}

internal interface WorkerScheduler {
  fun scheduleStaleWorker()

  fun rescheduleStale()

  fun cancelStaleWorker()

  fun scheduleMealWorker(
    mealId: Long,
    notificationDateTime: LocalDateTime,
  )

  fun cancelMealWorker(mealId: Long)
}
