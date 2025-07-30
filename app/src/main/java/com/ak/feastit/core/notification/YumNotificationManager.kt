// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.core.notification

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.ak.feastit.R
import com.ak.feastit.core.notification.PostNotificationBroadcastReceiver.Companion.NOTIFICATION_CHANNEL_ID
import com.ak.feastit.core.notification.PostNotificationBroadcastReceiver.Companion.NOTIFICATION_DEEP_LINK_URL
import com.ak.feastit.core.notification.PostNotificationBroadcastReceiver.Companion.NOTIFICATION_ID
import com.ak.feastit.core.notification.PostNotificationBroadcastReceiver.Companion.NOTIFICATION_IMAGE
import com.ak.feastit.core.notification.PostNotificationBroadcastReceiver.Companion.NOTIFICATION_MESSAGE
import com.ak.feastit.core.notification.PostNotificationBroadcastReceiver.Companion.NOTIFICATION_TITLE
import com.mak.feastit.domain.model.YumNotification
import com.mak.feastit.domain.model.YumNotificationChannel
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import timber.log.Timber

/**
 * Reference: [Tivi](https://github.com/chrisbanes/tivi/blob/main/core/notifications/core/src/androidMain/kotlin/app/tivi/core/notifications/AndroidNotificationManager.kt)
 */
internal class YumNotificationManager @Inject constructor(
  private val application: Application,
) : NotificationManager {
  private val notificationManager by lazy { NotificationManagerCompat.from(application) }
  private val alarmManager by lazy { application.getSystemService<AlarmManager>()!! }

  override suspend fun schedule(notification: YumNotification) {
//        Create notification channel
    notificationManager.createChannel(notification.channel)

    val windowStartTime = notification.dateTime - ALARM_WINDOW_LENGTH
    val now = defaultNow()
    if (windowStartTime <= now) {
      Timber.d("Notification is old: ${notification.dateTime} so schedule now: $now")
      val intent = PostNotificationBroadcastReceiver.buildIntent(application)
      intent.extraData(notification)
      application.sendBroadcast(intent)
    } else {
      Timber.d("Schedule notification for $notification")
      Timber.d("Schedule notification at instant $windowStartTime")
      // we need to have local date time to schedule alarm
      val localDateTimeInstant = windowStartTime.defaultLocalDateTime().toInstant(TimeZone.UTC)
      val epoch = localDateTimeInstant.epochSeconds
      Timber.d("Schedule at local date time: $localDateTimeInstant")
      Timber.d("Alarm Epoch: $epoch")
//            FIXME: Notification is triggered quickly
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, epoch, notification.buildPendingIntent(application))
      alarmManager.setWindow(
        // type
        AlarmManager.RTC_WAKEUP,
        // windowStartMillis. We minus our defined window from the provide date/time
        epoch,
        // windowLengthMillis
        ALARM_WINDOW_LENGTH.inWholeMilliseconds,
        // operation
        notification.buildPendingIntent(application),
      )
    }
  }

  override suspend fun cancel(notification: YumNotification) {
    alarmManager.cancel(notification.buildPendingIntent(application))
    Timber.d("Notification cancelled: ${notification.id}")
  }

  private fun NotificationManagerCompat.createChannel(channel: YumNotificationChannel) {
    val notificationChannel =
      NotificationChannelCompat
        .Builder(channel.id, NotificationManagerCompat.IMPORTANCE_DEFAULT)
        .apply {
          when (channel) {
            YumNotificationChannel.DEVELOPER -> {
              setName("Dev testing")
              setVibrationEnabled(true)
            }

            YumNotificationChannel.MEAL_PLANNING -> {
              setName(application.getString(R.string.notification_channel_meal_plan_title))
              setDescription(application.getString(R.string.notification_channel_meal_plan_summary))
              setVibrationEnabled(true)
            }
          }
        }.build()
    createNotificationChannel(notificationChannel)
  }

  private fun YumNotification.buildPendingIntent(application: Application): PendingIntent {
    val intent = PostNotificationBroadcastReceiver.buildIntent(application)
    intent.extraData(this)
    return PendingIntent.getBroadcast(application, id.hashCode(), intent, PENDING_INTENT_FLAGS)
  }

  private fun Intent.extraData(notification: YumNotification) {
    putExtra(NOTIFICATION_ID, notification.id)
    putExtra(NOTIFICATION_TITLE, notification.title)
    putExtra(NOTIFICATION_MESSAGE, notification.message)
    putExtra(NOTIFICATION_DEEP_LINK_URL, notification.deeplinkUrl)
    putExtra(NOTIFICATION_CHANNEL_ID, notification.channel.id)
    putExtra(NOTIFICATION_IMAGE, notification.image)
  }

  private companion object {
    // We request 10 mins as Android S can choose to apply a minimum of 10 mins anyway
    // Being earlier is better than being late
    val ALARM_WINDOW_LENGTH = 10.minutes
    private const val PENDING_INTENT_FLAGS = FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT or FLAG_ONE_SHOT
  }
}

internal interface NotificationManager {
  suspend fun schedule(notification: YumNotification)

  suspend fun cancel(notification: YumNotification)

  suspend fun cancelAll(notifications: List<YumNotification>) {
    notifications.forEach { cancel(it) }
  }

//    suspend fun cancelAllInChannel(channel: YumNotificationChannel) {
//        cancelAll(
//            getPendingNotifications().filter { it.channel == channel },
//        )
//    }
//
//    suspend fun getPendingNotifications(): List<YumNotification> = emptyList()
}
