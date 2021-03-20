package com.ak.feastit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [RecipeEntity::class, IngredientEntity::class, InstructionEntity::class],
        version = 1
)
abstract class FeastDatabase: RoomDatabase() {

    abstract fun recipeDAO(): RecipeDAO

}