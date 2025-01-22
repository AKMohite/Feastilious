package com.mak.feastit.domain.model

import kotlinx.datetime.Instant

data class YumNotification(
    val id: String,
    val title: String,
    val message: String,
    val channel: YumNotificationChannel,
    val image: String?,
    val date: Instant,
    val deeplinkUrl: String? = null,
)

enum class YumNotificationChannel(val id: String) {
    DEVELOPER("dev"),
    MEAL_PLANNING("meal_planning"),
    ;

    companion object {
        fun fromId(id: String): YumNotificationChannel {
            return YumNotificationChannel.entries.first { it.id == id }
        }
    }
}