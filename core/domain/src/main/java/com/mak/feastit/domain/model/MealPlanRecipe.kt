package com.mak.feastit.domain.model

import com.mak.feastit.domain.util.defaultLocalDateTime
import com.mak.feastit.domain.util.defaultNow
import kotlinx.datetime.LocalDateTime


data class MealPlanRecipe(
    val id: Long,
    val recipeId: Long,
    val scheduledFor: LocalDateTime?,
    val preparationTime: Int?,
    val isMade: Boolean,
    val name: String,
    val image: String
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
        if (now > scheduledFor) {
            return Pair(false, "${scheduledFor.time.hour}:${scheduledFor.time.minute}")
        }
        return Pair(true, preparation)
    }

    fun displayablePreparationTime() = "$preparationTime mins"

    fun isSameAs(other: MealPlanRecipe): Boolean {
        return recipeId == other.recipeId &&
                scheduledFor == other.scheduledFor &&
                isMade == other.isMade
    }
}
