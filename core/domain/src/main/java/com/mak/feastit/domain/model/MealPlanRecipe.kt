// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime

data class MealPlanRecipe(
  val id: Long,
  val recipeId: Long,
  val scheduledFor: LocalDateTime?,
  val preparationTime: Int?,
  val isMade: Boolean,
  val name: String,
  val image: RecipeImage?,
) {
  /**
   * @return preparation and true if @param[scheduledFor] is null or current datetime is before @param[scheduledFor]
   * else scheduled time and false is returned
   */
  fun preparationOrSchedule(): Pair<Boolean, String> {
//        TODO maybe move this logic to mapper?
    val preparation = displayablePreparationTime()
    if (scheduledFor == null) return Pair(true, preparation)
    val now = defaultNow().defaultLocalDateTime()
    if (now < scheduledFor && now.date == scheduledFor.date) {
      return Pair(false, "${scheduledFor.time}")
    }
    return Pair(true, preparation)
  }

  fun displayablePreparationTime() = "$preparationTime mins"

  fun isSameAs(other: MealPlanRecipe): Boolean = recipeId == other.recipeId &&
    scheduledFor == other.scheduledFor &&
    isMade == other.isMade

  fun toNotification(
    title: String = "",
    message: String = "",
    notifyAt: Instant = defaultNow(),
  ): YumNotification = YumNotification(
    id = "meal-plan-$id-$recipeId",
    title = title,
    message = message,
    channel = YumNotificationChannel.MEAL_PLANNING,
    dateTime = notifyAt,
    deeplinkUrl = null,
    image = image?.getResource(),
  )
}
