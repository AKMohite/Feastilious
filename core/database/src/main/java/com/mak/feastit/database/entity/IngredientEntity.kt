package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_AMOUNT
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_ID
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_NAME
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_TABLE
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_UNIT
import com.mak.feastit.database.util.Constants.DB_RECIPE_ID
import com.mak.feastit.database.util.Constants.DB_TABLE_COL_IMG
import com.mak.feastit.database.util.Constants.DB_TABLE_ID

@Entity(
        tableName = DB_INGREDIENT_TABLE
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DB_TABLE_ID)
    val id: String,
    @ColumnInfo(name = DB_INGREDIENT_ID)
    val ingredientId: Long,
    @ColumnInfo(name = DB_RECIPE_ID)
    val recipeId: Long,
    @ColumnInfo(name = "aisle_category")
    val aisle: String? = null,
    @ColumnInfo(name = DB_INGREDIENT_NAME)
    val ingredientName: String,
//    @ColumnInfo(name = DB_INGREDIENT_CONSISTENCY)
//    val ingredientConsistency: String,
    @ColumnInfo(name = DB_TABLE_COL_IMG)
    val ingredientImg: String,
    @ColumnInfo(name = DB_INGREDIENT_AMOUNT)
    val amount: Double,
    @ColumnInfo(name = DB_INGREDIENT_UNIT)
    val unit: String
)