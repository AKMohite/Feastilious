// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

import kotlin.time.Instant

data class YumNotification(
  val id: String,
  val title: String,
  val message: String,
  val channel: YumNotificationChannel,
  val image: String?,
  val dateTime: Instant,
  val deeplinkUrl: String? = null,
)

enum class YumNotificationChannel(
  val id: String,
) {
  DEVELOPER("dev"),
  MEAL_PLANNING("meal_planning"),
  ;

  companion object {
    fun fromId(id: String): YumNotificationChannel = YumNotificationChannel.entries.first { it.id == id }
  }
}
