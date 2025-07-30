// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.core.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import com.ak.feastit.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import androidx.core.net.toUri

internal class PostNotificationBroadcastReceiver : BroadcastReceiver() {
  override fun onReceive(
    context: Context,
    intent: Intent?,
  ) {
    Timber.d("On alarm triggered: $intent")
    val notificationId = intent?.getStringExtra(NOTIFICATION_ID)
    if (notificationId == null) {
      Timber.d("No notification id provided. Exiting.")
      return
    }
    val title = intent.getStringExtra(NOTIFICATION_TITLE)!!
    val message = intent.getStringExtra(NOTIFICATION_MESSAGE)!!
    val deepLink = intent.getStringExtra(NOTIFICATION_DEEP_LINK_URL)
    val image = intent.getStringExtra(NOTIFICATION_IMAGE) ?: ""
    val channelId = intent.getStringExtra(NOTIFICATION_CHANNEL_ID)!!
    val notificationManager = NotificationManagerCompat.from(context)
    val result = goAsync()

//        FIXME: Avoid globalscope
    GlobalScope.launch {
      val notification =
        NotificationCompat
          .Builder(context, channelId)
          // Replace this icon with something better
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentTitle(title)
          .setContentText(message)
          .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
          .setAutoCancel(true)
          .apply {
            if (deepLink != null) {
//                        TODO add deep link or handle intent
              setContentIntent(
                PendingIntent.getActivity(
                  context,
                  0,
                  Intent(Intent.ACTION_VIEW, deepLink.toUri()).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                  },
                  PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
                ),
              )
            }
          }

      if (image.isNotBlank()) {
        try {
          val request =
            ImageRequest
              .Builder(context)
              .data(image)
              .allowHardware(false) // Disable hardware bitmaps.
              .build()

          val drawable = context.imageLoader.execute(request).image?.toBitmap()
          val bitmap = drawable
//          val bitmap = (drawable as? BitmapDrawable)?.bitmap
          bitmap?.let {
            notification.setLargeIcon(it)
            Timber.d("Image loaded")
          }
        } catch (e: Exception) {
          Timber.e(e, "Notification image not loaded: $image")
        } finally {
        }
      }

      try {
        notificationManager.notify(notificationId, 0, notification.build())
        Timber.d("Notification triggered: $notificationId")
      } catch (se: SecurityException) {
        Timber.d(se, "Error posting notification")
      }
      result.finish()
    }
  }

  companion object {
    const val NOTIFICATION_ID = "notification-id"
    const val NOTIFICATION_TITLE = "notification-title"
    const val NOTIFICATION_MESSAGE = "notification-message"
    const val NOTIFICATION_DEEP_LINK_URL = "notification-deep-link"
    const val NOTIFICATION_IMAGE = "notification-image"
    const val NOTIFICATION_CHANNEL_ID = "notification-channel-id"

    fun buildIntent(context: Context): Intent = Intent(context, PostNotificationBroadcastReceiver::class.java)
  }
}
