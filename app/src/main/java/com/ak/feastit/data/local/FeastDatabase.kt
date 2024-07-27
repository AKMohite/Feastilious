package com.ak.feastit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.ak.feastit.data.local.dao.RecipeDAO
import com.ak.feastit.data.local.entity.IngredientEntity
import com.ak.feastit.data.local.entity.InstructionEntity
import com.ak.feastit.data.local.entity.RecipeEntity

@Database(
        entities = [RecipeEntity::class, IngredientEntity::class, InstructionEntity::class],
        version = 1
)
abstract class FeastDatabase: RoomDatabase(), FeastDB {

    override suspend fun blockTransaction(block: suspend () -> Unit) {
        this.withTransaction {
            block()
        }
    }
}

interface FeastDB {
    fun recipeDAO(): RecipeDAO

    suspend fun blockTransaction(block: suspend () -> Unit)
}
