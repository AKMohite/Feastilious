package com.mak.feastit.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.util.Constants.DB_RECIPE_ID
import com.mak.feastit.database.util.Constants.DB_TABLE_ID

data class RecipeDetailEntity(
    @Embedded
        val recipe: RecipeEntity,
    @Relation(
            parentColumn = DB_TABLE_ID,
            entityColumn = DB_RECIPE_ID
        )
        val ingredients: List<IngredientEntity>,
    @Relation(
                parentColumn = DB_TABLE_ID,
                entityColumn = DB_RECIPE_ID
        )
        val instructions: List<InstructionEntity>
)