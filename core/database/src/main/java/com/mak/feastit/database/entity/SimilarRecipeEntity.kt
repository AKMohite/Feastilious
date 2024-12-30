package com.mak.feastit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "similar_recipes"
)
data class SimilarRecipeEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val recipeId: Long,
    val parentRecipeId: Long
)
