package com.ak.feastit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ak.feastit.utils.*

@Entity(
        tableName = DB_INGREDIENT_TABLE
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DB_INGREDIENT_ID) // recipeId+ingredientName.removeWhiteSpaces
    val ingredientUnique: String,
    @ColumnInfo(name = DB_RECIPE_ID)
    val recipeId: Long,
    @ColumnInfo(name = DB_INGREDIENT_NAME)
    val ingredientName: String,
    @ColumnInfo(name = DB_INGREDIENT_CONSISTENCY)
    val ingredientConsistency: String,
    @ColumnInfo(name = DB_TABLE_COL_IMG)
    val ingredientImg: String,
    @ColumnInfo(name = DB_INGREDIENT_SPEC)
    val ingredientSpec: String,
    @ColumnInfo(name = DB_INGREDIENT_AMOUNT)
    val amount: Double,
    @ColumnInfo(name = DB_INGREDIENT_UNIT)
    val unit: String
)