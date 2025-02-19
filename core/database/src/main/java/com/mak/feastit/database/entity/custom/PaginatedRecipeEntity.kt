package com.mak.feastit.database.entity.custom

import androidx.room.ColumnInfo

data class PaginatedRecipeEntity(
    val id: Long,
    @ColumnInfo(name = "name")
    val recipeName: String,
    @ColumnInfo(name = "summary")
    val recipeSummary: String,
    @ColumnInfo(name = "img")
    val recipeImg: String,
    val page: Int
)
