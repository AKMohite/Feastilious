package com.ak.feastit.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ak.feastit.data.local.entity.IngredientEntity
import com.ak.feastit.data.local.entity.InstructionEntity
import com.ak.feastit.data.local.entity.RecipeEntity
import com.ak.feastit.utils.DB_RECIPE_ID
import com.ak.feastit.utils.DB_TABLE_ID

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