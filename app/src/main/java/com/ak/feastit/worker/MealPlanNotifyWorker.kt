package com.ak.feastit.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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
    private val mealPlanRepository: MealPlanRepository
): CoroutineWorker(appContext, workerParams) {

    companion object {
        const val TAG = "meal-plan-notify-worker"
        const val WORK_MEAL_ID = "work-meal-id"
        const val WORK_NOTIFICATION_TIME = "work-meal-notification-time"
    }

    override suspend fun doWork(): Result {
        val notificationTime = inputData.getString(WORK_NOTIFICATION_TIME) ?: throw IllegalArgumentException("No notification time found")
        val notifyAt = LocalDateTime.parse(notificationTime)
        val now = defaultNow()
        val workerTime = notifyAt.toInstant().minus(5.minutes)
        val diff = workerTime.minus(now)
        if (diff.isNegative() && diff.inWholeMinutes > 5) {
            Timber.d("The meal notification time is in the past")
//            False positive to avoid notification
            return Result.success()
        }
        val mealId = inputData.getLong(WORK_MEAL_ID, -1)
        if (mealId == -1L) throw IllegalArgumentException("No meal id found")
        val meal = mealPlanRepository.getMealPlanRecipe(mealId) ?: return Result.failure()
//        Create notification domain object and pass to notification alarm manager
        return Result.success()
    }
}