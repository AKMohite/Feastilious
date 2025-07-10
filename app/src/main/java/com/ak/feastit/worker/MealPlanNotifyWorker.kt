package com.ak.feastit.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ak.feastit.R
import com.ak.feastit.core.notification.NotificationManager
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.toInstant
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.LocalDateTime
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

@HiltWorker
internal class MealPlanNotifyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val mealPlanRepository: MealPlanRepository,
    private val notificationManager: NotificationManager
): CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORK_MEAL_ID = "work-meal-id"
        const val WORK_NOTIFICATION_TIME = "work-meal-notification-time"
    }
//    TODO cancel notification on work manager cancelled
//    override fun onStopped() {
//        super.onStopped()
//        notificationManager.cancel()
//    }

    override suspend fun doWork(): Result {
        Timber.d("Meal notifier started")
        val notificationTime = inputData.getString(WORK_NOTIFICATION_TIME) ?: throw IllegalArgumentException("No notification time found")
        val mealId = inputData.getLong(WORK_MEAL_ID, -1)
        if (mealId == -1L) throw IllegalArgumentException("No meal id found")
        val notifyAt = LocalDateTime.parse(notificationTime)
        Timber.d("Meal planned to notify at: $notifyAt")
        val now = defaultNow()
        val workerTime = notifyAt.toInstant().minus(5.minutes)
        val diff = workerTime.minus(now)
        if (diff.isNegative() && diff.inWholeMinutes > 5) {
            Timber.d("The meal notification time is in the past")
//            if app is force stopped the worker might be in enqueued so cancel
//            False positive to avoid notification
            return Result.success()
        }
        val mealPlan = mealPlanRepository.getMealPlanRecipe(mealId) ?: return Result.failure()
//        FIXME: notification title and message is too long so handled with String.take()
        val title = applicationContext.getString(R.string.notification_meal_plan_title, mealPlan.name.take(30))
        val message = applicationContext.getString(R.string.notification_meal_plan_message, mealPlan.name.take(15), notifyAt.time.toString())
        val notification = mealPlan.toNotification(title = title, message = message, notifyAt = notifyAt.toInstant())
//        TODO maybe we can remove alarm manager and handle notification in workmanager with initial delay
        notificationManager.schedule(notification)
        return Result.success()
    }

}
