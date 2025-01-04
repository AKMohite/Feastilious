package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
    tableName = "shopping_ingredients",
    foreignKeys = [
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShoppingEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DB_TABLE_ID)
    val id: String,
    @ColumnInfo("recipe_id")
    val recipeId: Long,
    @ColumnInfo(name = "is_bought")
    val isBought: Boolean
)