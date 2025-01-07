package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes"
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val recipeName: String,
    @ColumnInfo(name = "summary")
    val recipeSummary: String,
    @ColumnInfo(name = "img")
    val recipeImg: String,
    @ColumnInfo(name = "source")
    val recipeSource: String,
    @ColumnInfo(name = "ready_in_mins")
    val recipeReadyInMins: Int,
    @ColumnInfo(name = "servings")
    val servings: Int,
    @ColumnInfo(name = "price_per_serving")
    val pricePerServing: Double,
    @ColumnInfo(name = "source_name")
    val sourceName: String,
    @ColumnInfo(name = "is_fav")
    val isAddedToCollection: Boolean = false,
    @ColumnInfo(name = "cuisines")
    val cuisines: String,
    @ColumnInfo(name = "dish_types")
    val dishTypes: String,
    @ColumnInfo(name = "diets")
    val diets: String,
    @ColumnInfo(name = "caloric_breakdown")
    val caloricBreakdown: Map<String, String>
)