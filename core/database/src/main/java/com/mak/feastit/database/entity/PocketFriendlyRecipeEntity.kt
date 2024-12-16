package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pocket_friendly_recipes")
data class PocketFriendlyRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long,
    val page: Int
)
