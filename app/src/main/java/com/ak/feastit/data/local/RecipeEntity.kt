package com.ak.feastit.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ak.feastit.utils.*

@Entity(
    tableName = DB_RECIPE_TABLE,
    indices = [Index(value = [DB_RECIPE_ID], unique = true)]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DB_TABLE_ID)
    val id: Long,
    @ColumnInfo(name = DB_RECIPE_ID)
    val recipeId: Long,
    @ColumnInfo(name = DB_TABLE_COL_NAME)
    val recipeName: String,
    @ColumnInfo(name = DB_TABLE_COL_SUMMARY)
    val recipeSummary: String,
    @ColumnInfo(name = DB_TABLE_COL_IMG)
    val recipeImg: String,
    @ColumnInfo(name = DB_TABLE_COL_SOURCE)
    val recipeSource: String,
    @ColumnInfo(name = DB_RECIPE_READY_IN_MIN)
    val recipeReadyInMins: Int,
    @ColumnInfo(name = DB_RECIPE_SERVINGS)
    val servings: Int,
    @ColumnInfo(name = DB_RECIPE_PRICE)
    val pricePerServing: Double,
    @ColumnInfo(name = DB_RECIPE_SOURCE_NAME)
    val sourceName: String,
    @ColumnInfo(name = DB_MY_RECIPE_BOOK)
    val isAdded: Boolean = false,
)