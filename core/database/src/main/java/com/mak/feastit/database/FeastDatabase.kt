package com.mak.feastit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.mak.feastit.database.dao.LastSyncDAO
import com.mak.feastit.database.dao.PopularRecipeDAO
import com.mak.feastit.database.dao.RecipeDAO
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.RecipeEntity

@Database(
        entities = [RecipeEntity::class, IngredientEntity::class, InstructionEntity::class],
        version = 1
)
internal abstract class FeastDatabase: RoomDatabase(), FeastDB {

    override suspend fun blockTransaction(block: suspend () -> Unit) {
        this.withTransaction {
            block()
        }
    }
}

interface FeastDB {
    fun recipeDAO(): RecipeDAO

    fun popularRecipeDAO(): PopularRecipeDAO
    fun lastSyncDao(): LastSyncDAO

    suspend fun blockTransaction(block: suspend () -> Unit)
}
