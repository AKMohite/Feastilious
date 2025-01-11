package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "meal_planner",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long, // recipeId
    @ColumnInfo(name = "planned_for")
    val plannedFor: Instant?, // can be null and user can set this value later
    @ColumnInfo(name = "is_made")
    val isMade: Boolean
)
