package com.ak.feastit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ak.feastit.utils.*

@Entity(
    tableName = DB_RECIPE_TABLE
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DB_TABLE_ID)
    val id: Long,
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
    @ColumnInfo(name = DB_RECIPE_CUISINES)
    val cuisines: String,
    @ColumnInfo(name = DB_RECIPE_DISH_TYPES)
    val dishTypes: String,
    @ColumnInfo(name = DB_RECIPE_DIETS)
    val diets: String
)