// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.core.notification

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.mak.feastit.domain.model.YumNotification
import com.mak.feastit.domain.model.YumNotificationChannel
import com.mak.feastit.domain.util.defaultNow
import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.toInstant
import com.ak.feastit.compose.core.notification.PostNotificationBroadcastReceiver.Companion.extraData
import kotlinx.datetime.TimeZone
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import timber.log.Timber

private val ALARM_WINDOW_LENGTH = 10.minutes

internal class YumNotificationManager @Inject constructor(
  private val application: Application,
) : NotificationManager {
  private val notificationManager by lazy { NotificationManagerCompat.from(application) }
  private val alarmManager by lazy { application.getSystemService<AlarmManager>()!! }

  override suspend fun schedule(notification: YumNotification) {
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
      val localDateTimeInstant = windowStartTime.defaultLocalDateTime().toInstant(TimeZone.UTC)
      val epoch = localDateTimeInstant.epochSeconds
      Timber.d("Schedule at local date time: $localDateTimeInstant")
      Timber.d("Alarm Epoch: $epoch")
    }
  }

  override suspend fun cancel(notification: YumNotification) {
    notificationManager.cancel(notification.id.hashCode())
    val intent = PostNotificationBroadcastReceiver.buildIntent(application)
    val pendingIntent = PendingIntent.getBroadcast(
      application,
      notification.id.hashCode(),
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    alarmManager.cancel(pendingIntent)
  }
}

private fun NotificationManagerCompat.createChannel(channel: YumNotificationChannel) {
  if (Build.VERSION.SDK_INT >= 26) {
    val androidChannel = NotificationChannel(
      channel.id,
      channel.name,
      AndroidNotificationManager.IMPORTANCE_DEFAULT,
    )
    createNotificationChannel(androidChannel)
  }
}

internal interface NotificationManager {
  suspend fun schedule(notification: YumNotification)

  suspend fun cancel(notification: YumNotification)

  suspend fun cancelAll(notifications: List<YumNotification>) {
    notifications.forEach { cancel(it) }
  }
}
