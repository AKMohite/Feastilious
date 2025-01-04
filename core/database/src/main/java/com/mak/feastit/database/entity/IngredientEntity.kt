package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_ID
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_NAME
import com.mak.feastit.database.util.Constants.DB_INGREDIENT_TABLE
import com.mak.feastit.database.util.Constants.DB_RECIPE_ID
import com.mak.feastit.database.util.Constants.DB_TABLE_COL_IMG
import com.mak.feastit.database.util.Constants.DB_TABLE_ID

@Entity(
    tableName = DB_INGREDIENT_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = [DB_RECIPE_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
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
    @ColumnInfo(name = "quantity")
    val quantity: Double,
    @ColumnInfo(name = "unit")
    val unit: String
)